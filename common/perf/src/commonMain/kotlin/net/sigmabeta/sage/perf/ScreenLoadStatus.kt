package net.sigmabeta.sage.perf

data class ScreenLoadStatus(
    val name: String,
    val startTimeNanos: Long,
    val stageDurationMillis: Map<PerfStage, Long?> = emptyMap(),
)
