package net.sigmabeta.sage.ui.composables

/**
 * One-line summary of this error's top stack frame, shown only in the debug / inspection error
 * overlay (see [EmptyListIndicator]).
 *
 * A single common implementation over [Throwable.stackTraceToString]: the first line is the
 * exception itself, so the first non-blank line after it is the top frame on every target.
 */
internal fun Throwable.firstFrameSummary(): String =
    stackTraceToString()
        .lineSequence()
        .drop(1)
        .firstOrNull { it.isNotBlank() }
        ?.trim()
        ?: this::class.simpleName.orEmpty()
