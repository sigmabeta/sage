# `:android:firebase`

> Firebase Performance backend — per-screen, per-stage `Trace` recording behind `PerfBackend`.

The Android-only implementation of `common:perf`'s `PerfBackend`, backed by
Firebase Performance Monitoring. It opens a Firebase `Trace` per screen for each
`PerfStage` and stops them as stages complete.

## Contents

| File | What it is |
| --- | --- |
| `FirebasePerfBackend.kt` | `PerfBackend` impl. `startScreen` creates and starts one `Trace` per `PerfStage` (named `"$screen:$stage"`); `finishTrace` stops the trace for a given stage; `cancel` drops a screen's traces; `error` forwards a message to `Analytics.logError`. |

## Why depend on this module

Depend on `:common:perf` for the `PerfBackend`/`PerfStage` types; include
`:android:firebase` in the Android app to record performance traces in Firebase.
There are no DI bindings here — the app constructs `FirebasePerfBackend` with a
`FirebasePerformance` instance and an `Analytics` (for error reporting) and binds
it as the `PerfBackend`. The Firebase BoM and `firebase-performance` are exposed
`api` so consumers get the runtime.

## Using it

```kotlin
val perf: PerfBackend = FirebasePerfBackend(firebasePerformance, analytics)
perf.startScreen("Library")
perf.finishTrace("Library", PerfStage.LOADED)
```

## Module facts

- **Plugin:** `sage.android`
- **Targets:** Android only
- **Source set:** `src/main/java`
- **SAGE/module dependencies:** `api(projects.common.perf)`, `implementation(projects.android.coroutines)`; Firebase BoM + `firebase-performance`
