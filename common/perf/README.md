# `:common:perf`

> Screen-load, frame-time, and invalidate performance tracing for SAGE screens.

A small performance-measurement subsystem: it times how long a screen takes to
reach each load stage, aggregates per-screen frame and invalidate samples into
percentile stats, and streams the results as flows. It carries both the public
interfaces (`PerfMeasurer`, `PerfBackend`, `PerfSpec`) and their default
implementation (`PerfMeasurerImpl`) plus a `NoopBackend`.

## Contents

- **Measurer** — `PerfMeasurer.kt` is the interface a screen calls into (`start`,
  the `on*` stage hooks, `reportFrame`/`reportInvalidate`, and the three result
  streams). `PerfMeasurerImpl.kt` is the default impl: it tracks each screen's
  stage durations, runs a 30s failure timer (`TIMEOUT_SCREEN_LOAD`) that errors if
  not all stages arrive, and computes median/p95/p99 frame and invalidate stats on
  `requestUpdates()`.
- **Backend** — `PerfBackend.kt` is the sink the measurer reports trace lifecycle
  to (`startScreen`/`finishTrace`/`cancel`/`error`); `NoopBackend.kt` is the
  do-nothing implementation.
- **Spec / stages** — `PerfSpec.kt` (per-screen key with a `completionTargetTime`)
  and `PerfStage.kt` (the enum of load stages: `VIEW_CREATED`, `TITLE_LOADED`,
  `TRANSITION_START`, `PARTIAL_CONTENT_LOAD`, `FULL_CONTENT_LOAD`, plus the terminal
  `CANCELLATION`/`COMPLETION`).
- **Data / stats types** — `FrameInfo.kt` & `InvalidateInfo.kt` (raw samples),
  `FrameTimeStats.kt` & `InvalidateStats.kt` (aggregated percentile output), and
  `ScreenLoadStatus.kt` (a screen's name, start time, and per-stage durations).

## Why depend on this module

Depend on `:common:perf` when a screen or its host needs to record load timing and
jank metrics. The interfaces and impl live together here (no separate `:api`/`:real`
split), so callers code against `PerfMeasurer`/`PerfBackend`/`PerfSpec` and the app
graph supplies `PerfMeasurerImpl` with a concrete `PerfBackend` — `NoopBackend` when
tracing should be inert.

## Using it

```kotlin
val measurer: PerfMeasurer = PerfMeasurerImpl(NoopBackend(), dispatchers)

// `spec` is your screen's PerfSpec key.
measurer.start("Library", spec)
measurer.onViewCreated(spec)
measurer.onTitleLoaded(spec)
measurer.onFullContentLoad(spec)   // once all stages land, COMPLETION fires

// Feed samples, then ask for aggregated stats to be published:
measurer.reportFrame(FrameInfo(startTimeNanos, durationNanos, isJank = true), spec)
measurer.requestUpdates()
measurer.frameTimeStream().collect { statsBySpec -> /* … */ }
```

## Module facts

- **Plugin:** `sage.kmp`
- **Targets:** Android + JVM
- **Source set:** `commonMain` (deps declared on the `jvmSharedMain` source set; the
  impl uses `System.nanoTime()`, so it runs on JVM-backed targets)
- **SAGE/module dependencies:** `:common:coroutines`, `:common:analytics`
