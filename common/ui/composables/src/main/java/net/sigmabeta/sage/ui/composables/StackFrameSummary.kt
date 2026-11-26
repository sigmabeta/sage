package net.sigmabeta.sage.ui.composables

internal actual fun Throwable.firstFrameSummary(): String {
    val frame = stackTrace.first()
    return "${frame.fileName}: ${frame.lineNumber} (${frame.methodName})"
}
