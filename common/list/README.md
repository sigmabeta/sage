# `:common:list`

> The list/grid state machine — `ListState` → `ListStateActual`, driven by a screen ViewModel.

The shared abstractions every list- or grid-shaped screen is built on: an
abstract `ListState` you subclass to declare a screen's content, and a rendered
`ListStateActual` snapshot of immutable `ListModel`s. The state model is plain
Kotlin; each screen is driven by a normal `androidx.lifecycle.ViewModel` that the
feature contributes into Metro's ViewModel map and its composable resolves with
`metroViewModel<…>()`. It is an unsuffixed shared module (interfaces + base
classes, no `:real`/`:di` split of its own) — consumers subclass `ListState` and
build their own `SageScheduler` over the ViewModel's scope.

## Contents

- **Screen state** — `ListState.kt` (abstract base: subclass and override
  `title()` + `toListItems()`; `toActual()` renders, de-dupes via
  `checkForDupes`, and falls back to `ErrorStateListModel` on any throw;
  provides the `withStandardErrorAndLoading` / `sectionWithStandardErrorAndLoading`
  / `loading` / `error` helpers that turn an `LCE` into `ListModel`s).
  `ListStateActual.kt` is the rendered, immutable snapshot (`columnType`,
  `title`, `listItems`, `paginationType`) the UI consumes.
- **Screen view models** — the recommended way to drive a screen. There is no
  SAGE base ViewModel: a feature declares its own `androidx.lifecycle.ViewModel`
  that implements `ActionSink`, holds a `MutableStateFlow` of its `ListState`,
  and derives `uiStateActual` from it with `toActual()` (see
  [Using it](#using-it)). Metro contributes the ViewModel into the app graph's
  ViewModel map; the screen's `ViewModelStore` owns its lifetime and
  `viewModelScope` owns its work.
- **Legacy brain** — `ListViewModelBrain.kt` (abstract; the pre-Metro,
  platform-agnostic core of a list ViewModel: holds `internalUiState`/
  `uiStateActual` `StateFlow`s and a `uiEvents` `SharedFlow`, routes
  `sendAction`/`sendEvent` through `handleAction`/`handleEvent`, logs screen
  views/actions, auto-handles `DeviceBack`/`Resume`, and exposes `updateState`,
  `emitEvent`, `mapList`, `runInBackground`). `BrainProvider.kt` is its factory
  seam (`provideBrain(destination, scope)`). Kept for existing consumers only —
  **do not use it for new screens** (see
  [Legacy APIs](#legacy-listviewmodelbrain-and-brainprovider)).
- **Layout descriptors** — `ColumnType.kt` (`One` / `Regular` / `Staggered`,
  each computing `numberOfColumns(WidthClass)`), `WidthClass.kt`
  (`COMPACT`/`MEDIUM`/`EXPANDED` average widths), `PaginationType.kt`
  (`None` vs `Paginating` → `Standard`/`Reactive`, with `loadingType`/`pageSize`).
- **Scheduling seams** — `SageScheduler.kt` (bundles `SageDispatchers`,
  a `CoroutineScope`, and a `DelayManager`), `DelayManager.kt`
  (`shouldDelay()`, used by background loading to inject debug load delays).
- **Free functions** — `ListUtils.kt` (`checkForDupes`, the `ifTrue` / `ifNotNull`
  `NoopListModel` guards used while building item lists, and
  `ListStateActual.getErrors()`).

## Why depend on this module

Depend on `:common:list` to build a screen as a list or grid without
re-implementing state derivation, error/loading rendering, pagination
signalling, or column math. A feature's state type subclasses `ListState`, its
ViewModel turns that state into a `ListStateActual`, and the Compose layer in
`:common:ui:list-screens` consumes the result. This module owns the *logic*;
pair it with `:common:ui:list-screens` for the *rendering*. `LCE` comes from
`:common:appcomm`, which this module re-exports (`api`); `ListModel` comes from
`:common:ui:components`, which it does **not** re-export — add that dependency
yourself (see [Module facts](#module-facts)).

## Using it

The three pieces are the state, the ViewModel that drives it, and the composable
that renders it. The examples assume the feature module applies the
`sage.di` plugin (Metro) and depends on `metrox-viewmodel` for `@ViewModelKey`.

### 1. Declare the state

```kotlin
// 1. Declare the screen's content as a ListState subclass.
data class SongListState(val songs: LCE<List<Song>>) : ListState() {
    override val columnType = ColumnType.Regular(widthInDp = 160)

    override fun title(stringProvider: StringProvider) = TitleBarModel("Songs")

    override fun toListItems(stringProvider: StringProvider): List<ListModel> =
        when (val loaded = songs) {
            // Handle the error branch explicitly: see the caveat below.
            is LCE.Error -> error("songs", loaded.error)
            else -> loaded.withStandardErrorAndLoading { content.map(::songRow) }
        }
}
```

> **Error caveat.** `withStandardErrorAndLoading`'s `LCE.Error` branch currently resolves to the
> stdlib `error(...)`, which throws instead of returning an error row. `toActual()` catches that
> throw and renders its generic `"App Error Occurred"` fallback, so the operation name and the
> original throwable are lost. Route `LCE.Error` through the state's own
> `error(operationName, error)` helper before falling back to the standard helper, as above: it
> produces the `ErrorStateListModel` the helper was meant to build, with the operation name and
> throwable intact. This is a limitation of the current helper, not of the pattern.

### 2. Drive it with a ViewModel

```kotlin
// 2. A plain ViewModel, contributed into Metro's ViewModel map.
@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())
@ViewModelKey
@Inject
class SongListViewModel(
    private val stringProvider: StringProvider,
    private val analytics: Analytics,
    private val dispatchers: SageDispatchers,
    private val delayManager: DelayManager,
    private val repo: SongRepository,
) : ViewModel(), ActionSink {

    /** SAGE's coroutine seam, backed by [viewModelScope] instead of an app-wide scope. */
    private val scheduler = object : SageScheduler {
        override val dispatchers = this@SongListViewModel.dispatchers
        override val coroutineScope = viewModelScope
        override val delayManager = this@SongListViewModel.delayManager
    }

    // Your app's AnalyticsScreenId implementation.
    private val screenIdentifier = AppScreenIds.SONGS

    private fun initialState() = SongListState(LCE.Loading("songs"))
    private val internalUiState = MutableStateFlow(initialState())

    /** Rendered state: the [ListState] mapped through `toActual` on every change. */
    val uiStateActual: StateFlow<ListStateActual> = internalUiState
        .map { it.toActual(stringProvider) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = initialState().toActual(stringProvider),
        )

    init {
        sendAction(SageAction.InitNoArgs)
    }

    override fun sendAction(action: SageAction) {
        if (action.isInitAction()) analytics.logScreenView(action, screenIdentifier)
        else analytics.logAction(action, screenIdentifier)

        viewModelScope.launch(dispatchers.main) { handleAction(action) }
    }

    private fun handleAction(action: SageAction) {
        when (action) {
            SageAction.InitNoArgs -> loadSongs()
            else -> Unit
        }
    }

    private fun loadSongs() {
        repo.songs()                                    // Flow<List<Song>>
            .onEach { songs -> updateState { SongListState(LCE.Content(songs)) } }
            .runInBackground()
    }

    private fun updateState(updater: (SongListState) -> SongListState) {
        internalUiState.value = updater(internalUiState.value)
    }

    /**
     * Feature-local helper. SAGE does not ship `runInBackground`/`mapList`: they were
     * conveniences of the old brain, and each feature (or its shared ViewModel base)
     * carries its own copy, pinned to `viewModelScope`.
     */
    private fun <T> Flow<T>.runInBackground(): Job =
        flowOn(scheduler.dispatchers.disk).launchIn(viewModelScope)
}
```

Notes:

- `@ContributesIntoMap` + `@ViewModelKey` contribute a **factory** to the
  app-scope graph's ViewModel map; they do not make the ViewModel a singleton.
  The instance lives in the screen's `ViewModelStore`, and `viewModelScope` is
  cancelled when that screen goes away.
- `sage.di` puts `:common:di` on the classpath for `AppScope`; the feature must
  add `metrox-viewmodel` itself for `@ViewModelKey`/`metroViewModel`.
- `SageScheduler` is only a bundle of dispatchers, a `CoroutineScope`, and a
  `DelayManager`. Build one over `viewModelScope` (as above) so background work
  dies with the screen; the brain's app-wide scope has no such owner.
- The old brain auto-handled `SageAction.DeviceBack` and `SageAction.Resume`
  (chrome + title events). A ViewModel handles them explicitly if it needs
  them; note that `ListScreen`/`GridScreen` already send `SageAction.Resume`
  whenever the title is non-null.

### 3. Resolve and render it

```kotlin
@Composable
fun SongListScreen(modifier: Modifier = Modifier) {
    val viewModel = metroViewModel<SongListViewModel>()
    val state by viewModel.uiStateActual.collectAsStateWithLifecycle()

    ListScreen(
        state = state,
        actionSink = viewModel,          // the ViewModel is the ActionSink
        showDebug = false,
        sideMargin = 16.dp,
        modifier = modifier,
    ) { model, sink, debug, itemModifier, padding ->
        SongRow(model, sink, debug, itemModifier, padding)
    }
}
```

`metroViewModel<…>()` works because the app installs the contributed
`metroViewModelFactory` through `LocalMetroViewModelFactory`; there is no
per-screen graph accessor and no `BrainProvider` indirection.

## Legacy: `ListViewModelBrain` and `BrainProvider`

`:common:list` still ships `ListViewModelBrain` and its `BrainProvider` factory
seam for consumers that predate the ViewModel-per-screen structure. They are
**legacy APIs**, not the recommended path for new screens:

- The brain is a plain class resolved from the graph, with an app-wide
  `CoroutineScope` supplied through `SageScheduler`; its work is not tied to a
  screen's lifetime.
- A screen used to subclass the brain and expose it through a `ViewModel` shell,
  with `BrainProvider.provideBrain(destination, scope)` wired in a feature
  directory. Metro resolves each screen's ViewModel directly, so none of that
  indirection is needed.
- The brain's `uiEvents` flow, automatic `DeviceBack`/`Resume` handling, and
  built-in `runInBackground`/`mapList` helpers are its own; new ViewModels
  declare events and schedule work themselves.

New screens should not subclass `ListViewModelBrain` or implement
`BrainProvider`. Existing consumers can keep using them until they migrate.

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js`
- **Targets:** Android + JVM; JS (Node) when built with `-Psage.js`
- **Source set:** `commonMain`
- **SAGE/module dependencies:** `:common:appcomm`, `:common:analytics`,
  `:common:coroutines`, `:common:logging`, `:common:nav`, `:common:ui:strings`
  (all `api`), `:common:ui:components` (`implementation`); plus
  `kotlinx-collections-immutable` (`api`).
- **What is re-exported:** the `api` dependencies above are on your compile
  classpath transitively — including `LCE`/`SageAction`/`ActionSink` from
  `:common:appcomm` and `StringProvider` from `:common:ui:strings`.
  `:common:ui:components` is only an `implementation` detail here, so
  `ListModel` — the type `ListState.toListItems()` returns — is **not** visible
  through this module. A consumer must add `:common:ui:components` to its own
  dependencies.
