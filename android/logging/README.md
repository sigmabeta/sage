# `:android:logging`

> Android `Hatchet` — a logcat-backed implementation of SAGE's logging facade.

The Android-only implementation of `common:logging`'s `Hatchet` interface,
routing log calls to `android.util.Log`. As a `sage.android` platform module it
fulfils the multiplatform `Hatchet` contract with the Android backend.

## Contents

| File | What it is |
| --- | --- |
| `AndroidHatchet.kt` | `Hatchet` impl writing to `Log.println`/`Log.wtf`. Auto-derives a 16-char logcat tag from the calling stack frame, prefixes a fixed-width thread name, chunks messages over 4000 chars, and keeps the last 16 `HatchetError`s in a ring buffer (`recentErrors`). No-ops entirely unless `BuildConfig.DEBUG`. |

## Why depend on this module

Depend on `:android:logging` from the Android app to get a concrete `Hatchet`.
Other code depends on the `:common:logging` `Hatchet` interface; the Android app
constructs and binds `AndroidHatchet` so logging lands in logcat. The
`buildConfig = true` flag exists so the impl can read `BuildConfig.DEBUG` and
suppress all output in release builds. There are no DI bindings here —
construction/binding is left to the consumer.

## Using it

```kotlin
val hatchet: Hatchet = AndroidHatchet()
hatchet.d("starting work")

// Surface the most recent error-level logs (e.g. in a debug screen):
hatchet.recentErrors.forEach { println("${it.tag}: ${it.message}") }
```

## Module facts

- **Plugin:** `sage.android`
- **Targets:** Android only
- **Source set:** `src/main/java`
- **SAGE/module dependencies:** `api(projects.common.logging)`
