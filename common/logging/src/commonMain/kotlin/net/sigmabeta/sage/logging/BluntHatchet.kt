package net.sigmabeta.sage.logging

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * A Hatchet that doesn't print anything. Use in production. Still records errors in
 * [recentErrors] so a crash reporter can surface the last 16 — that's cheap and survives
 * having stdout logging disabled.
 */
class BluntHatchet : Hatchet {
    private val errorQueue = ArrayDeque<HatchetError>()

    override val recentErrors: List<HatchetError>
        get() = errorQueue.toList()

    override fun v(message: String) = Unit

    override fun d(message: String) = Unit

    override fun i(message: String) = Unit

    override fun w(message: String) = Unit

    override fun e(message: String) = recordError(message)

    override fun log(severity: Int, message: String) {
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

    companion object {
        private const val ERROR = 6
        private const val MAX_RECENT_ERRORS = 16
    }
}
