# `:common:ui:perf-compose`

> Composition-timing wrappers that log slow screens and components.

A pair of `@Composable` wrappers (`WithMeasurementScreen` / `WithMeasurementComponent`)
that `measureTime { content() }` and log the duration against warning/error thresholds,
plus the `LocalLogger` composition local that supplies the `Hatchet` they log through.
This is a leaf UI-perf utility module, not an `:api`/`:real` split.

## Contents

| File | What it is |
| --- | --- |
| `Logging.kt` | Everything in the module: `LocalLogger` (a `staticCompositionLocalOf<Hatchet>` defaulting to `BasicHatchet`), the `isPerfMeasurementEnabled` runtime toggle, the predefined `Duration` thresholds (`DURATION_THRESHOLD_*` for screen/component, device/preview), and the two `@Composable` measurement wrappers. |

`WithMeasurementScreen` always logs the measured duration (at info/warn/error by
threshold); `WithMeasurementComponent` stays quiet until the warning threshold is
crossed. Both short-circuit to a bare `content()` call when `isPerfMeasurementEnabled`
is `false` — the Android `Application` sets that flag to `BuildConfig.DEBUG` at startup
so release builds skip the measurement overhead (JVM/desktop default it to `true`).

## Why depend on this module

Depend on `:common:ui:perf-compose` when a screen or reusable component needs
"why is this frame slow?" visibility during development without pulling in a profiler.
The thresholds and toggle are shared so every screen reports on the same scale. Provide
a real `Hatchet` via `LocalLogger` at the top of the tree (otherwise it falls back to
`BasicHatchet` printing to stdout).

## Using it

```kotlin
WithMeasurementScreen(
    title = "Library",
    warningThreshold = DURATION_THRESHOLD_WARNING_SCREEN_DEVICE,
    errorThreshold = DURATION_THRESHOLD_ERROR_SCREEN_DEVICE,
) {
    LibraryContent(state)
}

// Provide the app's logger once, near the root:
CompositionLocalProvider(LocalLogger provides appHatchet) { /* ... */ }
```

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js` + `sage.compose.kmp`
- **Targets:** Android + JVM; JS (Node) when built with `-Psage.js`
- **Source set:** `commonMain`
- **SAGE/module dependencies:** `:common:logging` (`api`)
