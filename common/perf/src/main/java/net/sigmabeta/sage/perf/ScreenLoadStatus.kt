package net.sigmabeta.sage.perf

import java.util.EnumMap

data class ScreenLoadStatus(
    val name: String,
    val startTimeNanos: Long,
    val stageDurationMillis: Map<PerfStage, Long?> = EnumMap(PerfStage::class.java),
)
