package net.sigmabeta.sage.logging

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class BasicHatchet : Hatchet {
    private val errorQueue = ArrayDeque<HatchetError>()

    override val recentErrors: List<HatchetError>
        get() = errorQueue.toList()

    override fun v(message: String) = println("V: $message")

    override fun d(message: String) = println("D: $message")

    override fun i(message: String) = println("I: $message")

    override fun w(message: String) = println("W: $message")

    override fun e(message: String) {
        println("E: $message")
        recordError(message)
    }

    override fun log(severity: Int, message: String) {
        println("${severity.toSeverityCharacter()}: $message")
        if (severity >= ERROR) recordError(message)
    }

    @OptIn(ExperimentalTime::class)
    private fun recordError(message: String) {
        errorQueue.addLast(
            HatchetError(
                timestamp = Clock.System.now().toEpochMilliseconds(),
                tag = "",
                thread = "",
                message = message,
            ),
        )
        if (errorQueue.size > MAX_RECENT_ERRORS) errorQueue.removeFirst()
    }

    @Suppress("MagicNumber")
    private fun Int.toSeverityCharacter() = when (this) {
        2 -> 'V'
        3 -> 'D'
        4 -> 'I'
        5 -> 'W'
        6 -> 'E'
        else -> 'A'
    }

    companion object {
        private const val ERROR = 6
        private const val MAX_RECENT_ERRORS = 16
    }
}
