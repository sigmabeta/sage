package net.sigmabeta.sage.perf

data class FrameInfo(
    val startTimeNanos: Long,
    val durationOnUiThreadNanos: Long,
    val isJank: Boolean
)
