package net.sigmabeta.sage.logging

/**
 * One entry in a Hatchet's recent-errors queue. Captured at the call site so a debug UI or
 * crash reporter can surface the last 16 errors without losing context.
 *
 * `tag` / `thread` are empty for [BasicHatchet] / [BluntHatchet]: those impls don't walk the
 * stack or query the current thread (commonMain has no `Thread.currentThread()`).
 */
data class HatchetError(
    val timestamp: Long,
    val tag: String,
    val thread: String,
    val message: String,
)
