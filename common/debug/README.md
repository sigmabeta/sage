# `:common:debug`

> Reactive providers that surface debug-build toggles as `StateFlow`s.

Debug-build tooling that adapts the `Flow<Boolean>` settings from
`:common:settings:general`'s `DebugSettingsManager` into hot `StateFlow`s the UI can
read directly. Each provider seeds an initial value and keeps it in sync with the
stored setting on a background dispatcher.

## Contents

| File | What it is |
| --- | --- |
| `ShowDebugProvider.kt` | Exposes `showDebugFlow: StateFlow<Boolean>` mirroring `DebugSettingsManager.getShouldShowDebug()` — whether debug options should be visible. |
| `RenderOverlayProvider.kt` | Exposes `showRenderOverlayFlow: StateFlow<Boolean>` mirroring `getShouldShowRenderOverlay()` — whether the render/perf overlay should be drawn. |

## Why depend on this module

Depend on `:common:debug` when UI or other always-running components need a snapshot
+ reactive `StateFlow` of a debug toggle rather than collecting the raw settings
`Flow` themselves. Each provider takes a `DebugSettingsManager`, a `CoroutineScope`
in which to keep the mirror running, and `SageDispatchers`; it depends (privately) on
`:common:settings:general`.

## Using it

```kotlin
val provider = ShowDebugProvider(debugSettingsManager, appScope, dispatchers)

// Read the current value or collect changes:
if (provider.showDebugFlow.value) showDebugMenu()
provider.showDebugFlow.collect { visible -> /* … */ }
```

## Module facts

- **Plugin:** `sage.kmp`
- **Targets:** Android + JVM
- **Source set:** `commonMain` (deps declared on the `jvmSharedMain` source set)
- **SAGE/module dependencies:** `:common:coroutines`, `:common:settings:general` (`implementation`)
