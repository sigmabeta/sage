# `:common:list`

> The list/grid state machine — `ListState` → `ListStateActual` plus the `ListViewModelBrain` that drives a screen.

The shared abstractions every list- or grid-shaped screen is built on: an
abstract `ListState` you subclass to declare a screen's content, a rendered
`ListStateActual` snapshot of immutable `ListModel`s, and an abstract
`ListViewModelBrain` that owns the state/event/action plumbing. It is an
unsuffixed shared module (interfaces + base classes, no `:real`/`:di` split of
its own) — consumers subclass these and wire their own implementations of the
`SageScheduler`/`BrainProvider` seams.

## Contents

- **Screen state** — `ListState.kt` (abstract base: subclass and override
  `title()` + `toListItems()`; `toActual()` renders, de-dupes via
  `checkForDupes`, and falls back to `ErrorStateListModel` on any throw;
  provides the `withStandardErrorAndLoading` / `sectionWithStandardErrorAndLoading`
  / `loading` / `error` helpers that turn an `LCE` into `ListModel`s).
  `ListStateActual.kt` is the rendered, immutable snapshot (`columnType`,
  `title`, `listItems`, `paginationType`) the UI consumes.
- **The brain** — `ListViewModelBrain.kt` (abstract; the platform-agnostic core
  of a list ViewModel: holds `internalUiState`/`uiStateActual` `StateFlow`s and a
  `uiEvents` `SharedFlow`, routes `sendAction`/`sendEvent` through
  `handleAction`/`handleEvent`, logs screen views/actions, auto-handles
  `DeviceBack`/`Resume`, and exposes `updateState`, `emitEvent`, `mapList`,
  `runInBackground`). `BrainProvider.kt` is the factory seam
  (`provideBrain(destination, scope)`).
- **Layout descriptors** — `ColumnType.kt` (`One` / `Regular` / `Staggered`,
  each computing `numberOfColumns(WidthClass)`), `WidthClass.kt`
  (`COMPACT`/`MEDIUM`/`EXPANDED` average widths), `PaginationType.kt`
  (`None` vs `Paginating` → `Standard`/`Reactive`, with `loadingType`/`pageSize`).
- **Scheduling seams** — `SageScheduler.kt` (bundles `SageDispatchers`,
  a `CoroutineScope`, and a `DelayManager`), `DelayManager.kt`
  (`shouldDelay()`, used by `runInBackground` to inject debug load delays).
- **Free functions** — `ListUtils.kt` (`checkForDupes`, the `ifTrue` / `ifNotNull`
  `NoopListModel` guards used while building item lists, and
  `ListStateActual.getErrors()`).

## Why depend on this module

Depend on `:common:list` to build a screen as a list or grid without
re-implementing state flow, error/loading rendering, pagination signalling, or
column math. A feature's ViewModel subclasses `ListViewModelBrain` and its state
type subclasses `ListState`; the Compose layer in `:common:ui:list-screens`
consumes the resulting `ListStateActual`. This module owns the *logic*; pair it
with `:common:ui:list-screens` for the *rendering*. `ListModel`/`LCE` come from
`:common:ui:components` and `:common:appcomm`, which this module re-exports
(`api`).

## Using it

```kotlin
// 1. Declare the screen's content as a ListState subclass.
data class SongListState(val songs: LCE<List<Song>>) : ListState() {
    override val columnType = ColumnType.Regular(widthInDp = 160)
    override val paginationType = PaginationType.Standard()

    override fun title(stringProvider: StringProvider) = TitleBarModel("Songs")

    override fun toListItems(stringProvider: StringProvider): List<ListModel> =
        songs.withStandardErrorAndLoading { content.map(::songRow) }
}

// 2. Drive it with a ListViewModelBrain subclass.
class SongListBrain(
    stringProvider: StringProvider,
    analytics: Analytics,
    hatchet: Hatchet,
    scheduler: SageScheduler,
    private val repo: SongRepository,
) : ListViewModelBrain(stringProvider, analytics, hatchet, scheduler) {

    override val screenIdentifier = AnalyticsScreenId.SONGS
    override fun initialState() = SongListState(LCE.Loading("songs"))

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.Init -> repo.songs()
                .mapList(::toModel)
                .runInBackground()        // off the main dispatcher, with optional debug delay
            is SageAction.LoadMoreRequested -> loadNextPage()
            else -> Unit
        }
    }

    private fun onSongs(songs: List<Song>) =
        updateState { SongListState(LCE.Content(songs)) }   // recomputes ListStateActual
}
```

The UI then collects `brain.uiStateActual` and renders it with `ListScreen` /
`GridScreen` from `:common:ui:list-screens`.

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js`
- **Targets:** Android + JVM; JS (Node) when built with `-Pchipbox.js`
- **Source set:** `commonMain`
- **SAGE/module dependencies:** `:common:appcomm`, `:common:analytics`,
  `:common:coroutines`, `:common:logging`, `:common:nav`, `:common:ui:strings`
  (all `api`), `:common:ui:components` (`implementation`); plus
  `kotlinx-collections-immutable` (`api`).
