# `:common:settings:general`

> Typed accessors over key-value `Storage` for general and debug app settings.

Two thin managers that wrap `:common:storage:common`'s `Storage` to expose named
boolean settings as read/write pairs. Reads come back as `Flow`s (so callers react
to changes); writes persist immediately. Booleans are stored as their string form.

## Contents

| File | What it is |
| --- | --- |
| `GeneralSettingsManager.kt` | App-wide prefs: `getKeepScreenOn`/`setKeepScreenOn` (default `true`) and `getNeedsAutoMigrate`/`setNeedsAutoMigrate` (default `true`). |
| `DebugSettingsManager.kt` | Debug-build toggles: show-debug-options, use-fake-api, delay-loading-ops, show-snackbars, and show-render-overlay — each a `get…(): Flow<Boolean>` / `set…(value)` pair, all defaulting to `false`. |

## Why depend on this module

Depend on `:common:settings:general` wherever code needs to read or flip these
specific settings. `DebugSettingsManager` in particular is the backing store for the
debug providers in `:common:debug` (which depends on this module). Both managers take
a `Storage`, so the storage layer must be wired below them in the graph.

## Using it

```kotlin
val settings = GeneralSettingsManager(storage)

// Reactively observe a setting:
settings.getKeepScreenOn().collect { keepOn -> /* … */ }

// Persist a new value:
settings.setKeepScreenOn(false)
```

## Module facts

- **Plugin:** `sage.kmp`
- **Targets:** Android + JVM
- **Source set:** `commonMain` (deps declared on the `jvmSharedMain` source set)
- **SAGE/module dependencies:** `:common:coroutines`, `:common:storage:common`
