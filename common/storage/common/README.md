# `:common:storage:common`

> The `Storage` key/value persistence interface — SAGE's platform-agnostic settings abstraction.

A single `:api`-style interface for reading and writing simple typed values
(strings, ints) by key, with reactive `Flow` reads. It declares the contract;
platform-backed implementations (e.g. DataStore/preferences) live elsewhere and
are wired separately. Exists so common code can persist and observe small bits of
state without depending on any platform storage library.

## Contents

| File | What it is |
| --- | --- |
| `Storage.kt` | `interface Storage`: `saveString`/`saveInt` writers plus `savedStringFlow(key)` / `savedIntFlow(key)` returning a `Flow<String?>` / `Flow<Int?>` that emits the current value and subsequent updates. |

## Why depend on this module

Depend on `:common:storage:common` when common/feature code needs to persist or
observe a small piece of user/app state (a setting, a flag, a last-used value)
and must stay multiplatform. You depend on this for the type; the app wires a
concrete `Storage` implementation through DI. It `api`-exposes
`:common:coroutines` so consumers get `Flow` without an extra dependency.

## Using it

```kotlin
class ThemeRepository(private val storage: Storage) {
    val themeName: Flow<String?> = storage.savedStringFlow(KEY_THEME)

    fun setTheme(name: String) = storage.saveString(KEY_THEME, name)

    companion object { private const val KEY_THEME = "theme" }
}
```

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js`
- **Targets:** Android + JVM; JS (Node) when built with `-Psage.js`
- **Source set:** `commonMain`
- **SAGE/module dependencies:** `:common:coroutines` (`api`)
