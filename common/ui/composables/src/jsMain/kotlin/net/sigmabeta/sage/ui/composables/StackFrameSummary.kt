package net.sigmabeta.sage.ui.composables

// JS has no JVM StackTraceElement; fall back to the first non-empty line of the rendered trace.
// (Debug/inspection-only overlay — never exercised at runtime on this target.)
internal actual fun Throwable.firstFrameSummary(): String =
    stackTraceToString().lineSequence().map { it.trim() }.firstOrNull { it.isNotEmpty() } ?: ""
