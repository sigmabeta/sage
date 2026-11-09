# Coding standards

These are the app-architecture rules for applications built on SAGE. ktlint, detekt and Android
lint already enforce formatting and the mechanical rules; this file covers the structural judgement
calls they cannot. The canonical worked example is the SAGE Sample app (`sage-sample`): read its
README and module READMEs before starting a new app or screen.

## Apps are KMP-first

SAGE is a Kotlin Multiplatform stack. Treat multiplatform as the default and the platform as the
exception.

- Any module that holds app behaviour is a KMP module (`sage.kmp`, plus `sage.compose.kmp` when it
  has UI and `sage.kmp.js` when the browser app must consume it). Screens, ViewModels, navigation
  policy, models, strings and business rules live in `commonMain`.
- An application module (`:apps:android`, `:apps:jvm`, `:apps:js`, `:apps:server`) is a composition
  root and should stay as small as possible. Platform-specific responsibilities it may keep:
  - the entry point and window lifecycle;
  - the Metro dependency graph and the binding of platform seams;
  - the platform storage backend, HTTP client and local-storage implementation;
  - platform Back input and bundled font or resource loading.

  If a piece of code is not inherently platform-specific, it does not belong in an app module.
- Write platform-specific code (`androidMain`, `jvmMain`, `jsMain`) only when absolutely necessary —
  that is, when the platform API forces it. Define the seam once in `commonMain` and supply an
  `actual` per target; never fork a whole screen or ViewModel per platform.
- Place code in the narrowest source set it compiles in: pure Kotlin in `src/commonMain/kotlin`,
  code that needs `java.*` in `src/main/java` (the shared `jvmSharedMain` source set), and genuine
  platform code in `androidMain`/`jvmMain`/`jsMain`.
- Never duplicate shared code between apps. Move it into a shared module (`:core:*`, a feature's
  `:api`/`:real`, `:core:ui:*`, `:core:strings:*`) and let each app bind it. An app may keep only
  the pieces the other apps cannot share.
- A shared contract must compile for every target its consumers use. If an implementation needs a
  JVM-only dependency, keep it in its own module and bind a target-appropriate implementation;
  never make a feature module or repository interface depend on it.

## List and grid screens load through a repository abstraction

Every list or grid screen gets its content from a repository interface; the screen never owns a
data source.

- Define the interface in the feature's or domain's `:api` module. Return SAGE `LCE` values
  (loading / content / error), and take an explicit pagination window (`load(offset, limit)`) when
  the screen pages. Empty is a content state, and a missing item or a failure is a repository
  result or typed error — not an exception thrown into the UI.
- The ViewModel receives the repository through Metro and maps its results into the screen's
  `ListState`. `toListItems` only shapes rows: it does not generate data, filter over a whole
  catalog, or fetch.
- A feature module must not import a demo generator, a fixture, an HTTP client or a platform data
  source. Only the app graph and the `:di` module know which implementation is bound.
- Keep each implementation in its own module, and provide a deterministic `:fake` for tests. A
  local/demo implementation and a remote implementation are separate modules so the native and
  browser apps can bind the same interface to different backends without a feature knowing.
- Every model that represents a domain row gets a stable `dataId` derived from the domain identity,
  and ids must be unique within the screen. Never use the row index: SAGE throws on duplicate ids at
  render time, and a positional id breaks selection, diffing and paging.
- A paged screen declares SAGE's `PaginationType` and fetches windows. Appending or prepending must
  dedupe by the domain id, keep the rows already on screen when a page fails, and stop at a short
  or empty page.
- Represent loading, empty and error as `LCE` and render them with SAGE's standard list rows. Route
  `LCE.Error` through the state's own `error(operationName, error)` helper before the generic
  helper, so the operation name and cause survive (see `:common:list`'s error caveat).
- The sample is the reference: `:core:repository:api` holds the interfaces,
  `:core:repository:demo` and `:core:repository:remote` hold the implementations, and the
  `listitems`, `browse` and `search` features read them through their ViewModels. Its screenshot
  modules build a `ListState` by hand rather than calling a repository.

## Split a logical module into api, fake, real and di variants

A logical module is a directory that fans out into suffixed Gradle modules. Keep the contract and
each implementation apart so a consumer can depend on the contract without dragging in a backend.

| Variant | Contains | Builds |
|---|---|---|
| `:api` | Interfaces, value types, DTOs, route keys, state/action types, string ids | KMP (`sage.kmp`); this is the only variant other modules depend on for types |
| `:real` | The production implementation of `:api` | KMP; use `androidMain`/`jvmMain`/`jsMain` only for a genuine platform seam |
| `:fake` | Deterministic, in-memory test doubles implementing `:api` | KMP; no I/O and no clock |
| `:di` | Metro bindings that select the implementation | The targets its consumers need; plain `sage.di` setup, no logic |
| `:native` | Build/packaging companion for a `:real` that wraps a native library | Android-only CMake/NDK leaf; host targets build the same source tree |

A feature with Paparazzi goldens adds an Android-only `:screenshot` companion. It is a test module
and never a runtime dependency.

Rules:

- Dependencies flow `:api` ← `:real`/`:fake` ← `:di` ← app. A feature depends on `:api`, never on
  `:real` or `:fake`; only the app graph and `:di` choose an implementation.
- Name the Gradle paths by role: `:features:<screen>:{api,real,fake,di}` or
  `:core:<domain>:{api,real,fake,di}`. Route keys and the screen's public state/action types live in
  `:api`.
- Create only the variants the module needs, and omit a variant rather than leave an empty module.
  Never put an interface and its production implementation in the same module.
- `:di` is a binding container: Metro contributions and `@Provides` only. It owns no business
  logic, screen state or rendering.

A `:real` that wraps a native core adds a `:native` companion. It is build and packaging glue, never
an API:

- `:real` declares it `runtimeOnly` for Android and host-builds the same native source tree for the
  JVM target. Nothing compiles against `:native`.
- The JNI surface is one thin object of `external` functions plus `loadNativeLib`; no pointer, handle
  or platform type escapes into `commonMain`.
- Every native call is confined to one thread. Shared code owns the dispatcher and the lifecycle, and
  a test asserts the confinement.
- A native change is verified on host and device by rendering the same corpus through both builds and
  diffing the output.

## DI is Metro, and screen ViewModels are screen-scoped

- DI is Metro (`dev.zacsweers.metro`), with `AppScope` (`:common:di`) as the application scope. Do
  not introduce Hilt or Dagger.
- A screen ViewModel is contributed with
  `@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())` + `@ViewModelKey` and
  resolved with `metroViewModel<…>()`. Do not add a per-screen graph accessor, and do not use a
  `ListViewModelBrain`/`BrainProvider` shell for a new screen.
- The app-scope contribution is a factory, not the instance. The screen's `ViewModelStore` owns the
  instance: it survives while the screen stays in the back stack and is cleared when the screen is
  popped. Never mark a screen ViewModel `@SingleIn`; popping and re-pushing must build a fresh one.
- `viewModelScope` owns the screen's work. App-lifetime services are `@SingleIn(AppScope::class)`
  and are injected into the ViewModel.

## The feature owns state; the app owns the shell

- A feature module owns its screen's state machine: a `ListState` subclass, a ViewModel, actions
  and analytics. It does not render, navigate, or know about the host platform.
- The shared shell resolves the ViewModel, observes `uiStateActual`, and renders it with SAGE's
  `ListScreen` or `GridScreen`. It owns the composition root, tabs, top bar, chrome and insets.
- Features emit `SageEvent`s (`NavigateTo`, `NavigateBack`, …); the app translates them through
  shared policy. Never navigate from a feature module or import the navigation library into one.
- Back is one policy shared by the top-bar arrow, platform Back and `SageEvent.NavigateBack`. Do
  not implement a second back stack in an app or feature.
- Derive chrome from the active screen's `ListStateActual.title` and layout from its `ColumnType`
  plus the window width class: one column renders `ListScreen`, more render `GridScreen`. Keep that
  policy in a shared pure module, not in each app.
- The host sends exactly one foreground `Resume`/`Pause` pair per visible screen. The screen's base
  ViewModel coalesces SAGE's title-change `Resume` so feature side effects do not run twice.
- Route arguments are typed values passed to the screen and ViewModel. Do not parse route strings
  inside a feature or reach for `SavedStateHandle`.

## App-lifetime services own their state and scope

A screen owns its presentation state, not long-lived work. A service that outlives a screen —
playback, sync, downloads, a connection — is `@SingleIn(AppScope::class)`, owns its own
`CoroutineScope`, and is injected into the ViewModels that observe it.

- Expose immutable state as `StateFlow` and one-shot effects as `SharedFlow`. A screen projects that
  state; it never keeps a second writable copy.
- Control methods are imperative entry points that dispatch onto the service scope. The screen
  sends intents and renders the result.
- Confine the mutable model to one dispatcher (`limitedParallelism(1)`) or a single actor; no locks.
  Blocking and platform work runs on an injected dispatcher, never Main.
- The service must run headless: no Compose, navigation, resources or Android types. A platform sink
  (audio output, foreground service, notification) sits behind a shared interface bound by the app.
- The app or service starts and stops it, not a screen's `onResume`; define what happens while no
  screen is attached. Failures are state or events, not exceptions thrown into the UI.

## Routes and URLs come from one pure codec

- Define each destination once as a shared type in a KMP module, with its route key in the
  feature's `:api`. Encode and decode it in one pure codec used by navigation, browser history and
  deep links; do not repeat route strings or a URL grammar per app.
- A parameterized destination must have a screen/ViewModel identity that includes its arguments, or
  its own ViewModel store. Otherwise two different arguments share one ViewModel; test that pushing
  two values produces two states.
- Platform navigation is a seam: the shared shell decides the destination, and the app supplies the
  platform handler (Android Back, desktop keys and mouse back, browser `pushState`/`popstate`).

## User-visible text goes through one vocabulary

- Every user-visible string — screens, empty and error states, navigation labels, accessibility
  labels, and demo or repository content — is an app string id implementing SAGE's `SageStringId`,
  resolved through `StringProvider`. The app owns one exhaustive id → text table; feature modules
  emit ids and never literal English.
- The table may be split across per-area resource files, including Compose Multiplatform resources.
  What matters is one id type and one provider, not one file: do not keep a per-module vocabulary.
- Do not resolve platform resources in feature code. An Android app's only manifest string resource
  is the application label.
- A message that embeds a path, name or exception keeps its English in the resource with a `%1$s`
  placeholder; do not concatenate translated fragments.
- Icon-only controls take their accessibility label from the same table.
- ViewModels and other non-composable code take `StringProvider`; they cannot use
  `stringResource`/`R.string`.
- Repository diagnostics, exception messages and log lines are not UI text and may stay literals.
- The composition root installs the provider where the renderer suite can read it, and previews and
  screenshot fixtures use the same implementation so text cannot drift.
- Where a mechanical check exists, it flags a literal user-facing argument at the standard
  snackbar/event sink and names `file:line`; tests, previews and logs are excluded.

## Screenshot tests are fixtures, not running screens

- A `:features:<x>:screenshot` module is an Android-only test leaf. Its previews render a hand-built
  `ListState` through the shared preview harness; they never construct a ViewModel, collect a
  repository, read the clock or DI, or exercise the simulated delay.
- Build fixture rows with the deterministic fake generator plus the shared string provider and
  theme. Hardcoded names and captions, or a per-module theme copy, make a golden drift for reasons
  unrelated to the UI.
- Add both halves of a state: a `@DevicePreviews` composable and a matching Paparazzi `@Test`.
  Check goldens with the check task; record only for an intentional UI change, then review the diff.
- The screenshot module is never a runtime dependency of an app.

## Verification is part of "done"

A green build, lint and unit-test run is necessary, not sufficient. A change is done when the gates
it touches have run on their target.

- A regression test must fail on the unfixed code. Revert the fix locally, confirm the test goes red,
  then keep it.
- Closed-set mappings get a table test that names every case; a typed intent mapped onto an existing
  action gets one test per branch.
- UI tests drive the real shell over a fake graph with seeded, deterministic data, and run the same
  specs on the JVM host and a device. Assert typed destinations and events, not generic text.
  Existing tests are the source of truth: never weaken or delete one to make a change pass.
- Fixtures never read the clock, network or dependency graph; a failure produces a screenshot and a
  semantics dump.
- A change to native or rendered output is verified by rendering the same corpus through both builds
  and diffing the result, not only by a build.
- A device, browser, WASM or release gate is done when it has run on that target. Record what ran and
  what a human must still run in the change description.

## Every Gradle module has a README

The module README is its orientation surface: what it contains, why a consumer would depend on it,
its plugin, targets and source sets, its dependencies and why, its public API, and what its tests
pin down.

- Add a README with every new module.
- Update it when the public API, dependencies, plugins or role change.
- Keep it orientation, not a changelog or a copy of the root README.

## Use the sample as the reference

- Point at `sage-sample` when you need an example of a screen, a repository seam, an app graph or a
  module split. It is the public reference implementation and is kept current with SAGE. Its
  `CODING_STANDARDS.md` holds the review-time rules, such as closed-set table tests,
  intent-to-action branch tests, and comments describing code rather than merge mechanics.
- Do not refer to any other SAGE app by name in source, comments, KDoc, module names, resource
  names or test names. A consumer must be able to build a clean app from SAGE plus the sample
  without reading a sibling app.
- Keep SAGE itself app-agnostic. If a pattern is genuinely reusable, add it to SAGE with its README
  and tests; otherwise keep it in the app or the sample. Do not name app-specific types or screens
  after the app they came from.
