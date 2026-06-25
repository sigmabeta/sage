# `:common:coroutines`

> Shared coroutine primitives: a dispatcher bundle and a couple of `Flow` helpers.

Two small multiplatform building blocks used anywhere SAGE schedules background
work. A leaf module — it depends on nothing but `kotlinx-coroutines-core` (which
it re-exports via `api`).

## Contents

| File | What it is |
| --- | --- |
| `SageDispatchers.kt` | A `data class` bundling the four dispatchers the app uses: `computation`, `disk`, `network`, `main`. Inject this instead of touching `Dispatchers.*` directly so tests can swap in test dispatchers. |
| `CustomFlows.kt` | `CustomFlows.emitOnInterval(intervalMillis) { … }` — a `Flow` that runs `emitter` and emits its result every `intervalMillis`, forever. |

## Why depend on this module

Depend on `:common:coroutines` when a module needs the canonical dispatcher set
or the interval flow. The point of `SageDispatchers` is **testability and
platform independence**: production code never names a concrete dispatcher, so a
unit test can inject `StandardTestDispatcher` for all four, and a platform can
choose its own `main` dispatcher (Android Main vs. an event loop on the JVM).

## Using it

```kotlin
class Repository(private val dispatchers: SageDispatchers) {
    suspend fun load() = withContext(dispatchers.disk) { readFromDb() }
}

// A heartbeat that re-reads progress every 500 ms:
CustomFlows.emitOnInterval(intervalMillis = 500) { scanner.progressSnapshot() }
    .collect { progress -> /* … */ }
```

`SageDispatchers` is constructed once at the composition root with the
platform's real dispatchers and provided through DI.

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js`
- **Targets:** Android + JVM; JS (Node) when built with `-Pchipbox.js`
- **Source set:** `commonMain`
- **SAGE dependencies:** none (leaf) — re-exports `kotlinx-coroutines-core`
