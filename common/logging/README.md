# `:common:logging`

> Hatchet — SAGE's tiny multiplatform logging facade.

A logger interface (`Hatchet`) and two zero-dependency implementations. This is
the keystone leaf of the SAGE graph: almost every other module logs through it,
so it was the first library converted to `sage.kmp` and it pulls in nothing but
the Kotlin stdlib.

## Contents

| File | What it is |
| --- | --- |
| `Hatchet.kt` | The logging interface: `v/d/i/w/e(message)` plus `log(severity, message)` and a `recentErrors` view. |
| `BasicHatchet.kt` | Prints every level to stdout (`V: …`, `D: …`, …). Use in debug/CLI builds. |
| `BluntHatchet.kt` | Prints nothing. Use in production. Still records the last 16 error-level messages. |
| `HatchetError.kt` | One captured error: `timestamp`, `tag`, `thread`, `message`. |

Both impls keep a ring buffer of the last 16 messages logged at severity ≥ `ERROR`
(exposed via `recentErrors`) so a crash reporter or debug screen can surface them
even when stdout logging is off. `tag`/`thread` are empty here — `commonMain` has
no `Thread.currentThread()`; platform impls downstream fill them in.

## Why depend on this module

Depend on `:common:logging` whenever you need to log and want to stay
multiplatform. It has **no SAGE dependencies** and no third-party deps, so it is
safe to put at the bottom of any module's dependency list without dragging weight
in. Provide a `Hatchet` instance via DI rather than constructing one inline.

## Using it

```kotlin
class Thing(private val hatchet: Hatchet) {
    fun doWork() {
        hatchet.d("starting work")
        try {
            // ...
        } catch (e: Exception) {
            hatchet.e("work failed: ${e.message}")   // also lands in recentErrors
        }
    }
}

// Pick an impl at the composition root:
val hatchet: Hatchet = if (appInfo.isDebug) BasicHatchet() else BluntHatchet()
```

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js`
- **Targets:** Android + JVM; JS (Node) when built with `-Pchipbox.js`
- **Source set:** `commonMain` (pure Kotlin — `println` + a `when` on `Int`)
- **SAGE dependencies:** none (leaf)
