package net.sigmabeta.sage.ui.composables

/**
 * One-line "file: line (method)" summary of this error's top stack frame, shown only in the
 * debug / inspection error overlay (see [EmptyListIndicator]). JVM/Android read the JVM
 * StackTraceElement directly; the JS target falls back to the first rendered stack line.
 */
internal expect fun Throwable.firstFrameSummary(): String
