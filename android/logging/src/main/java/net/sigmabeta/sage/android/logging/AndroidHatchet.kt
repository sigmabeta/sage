package net.sigmabeta.sage.android.logging

import android.util.Log
import net.sigmabeta.sage.logging.BuildConfig
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.logging.HatchetError
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern

class AndroidHatchet : Hatchet {
    override fun v(message: String) {
        log(Log.VERBOSE, message)
    }

    override fun d(message: String) {
        log(Log.DEBUG, message)
    }

    override fun i(message: String) {
        log(Log.INFO, message)
    }

    override fun w(message: String) {
        log(Log.WARN, message)
    }

    override fun e(message: String) {
        log(Log.ERROR, message)
    }

    override fun log(severity: Int, message: String) {
        logInternal(severity, message)
    }

    private val errorQueueLock = Any()
    private val errorQueue = ArrayDeque<HatchetError>()

    override val recentErrors: List<HatchetError>
        get() = synchronized(errorQueueLock) { errorQueue.toList() }

    /**
     * Break up `message` into maximum-length chunks (if needed) and send to either
     * [Log.println()][Log.println] or
     * [Log.wtf()][Log.wtf] for logging.
     *
     * {@inheritDoc}
     */
    @Suppress("ThrowingExceptionsWithoutMessageOrCause")
    private fun logInternal(severity: Int, message: String) {
        if (!BuildConfig.DEBUG) {
            return
        }

        val element = Throwable().stackTrace.firstOrNull { it.className !in fqcnIgnore }
        val tagPair = element?.let(::resolveTag)
        val tag = tagPair?.formatted
        val threadName = currentFormattedThreadName()
        val string = "$threadName || $message"

        emit(severity, tag, string)

        if (severity >= Log.ERROR) {
            recordError(message, tagPair?.raw ?: FALLBACK_TAG_RAW)
        }
    }

    private fun emit(severity: Int, tag: String?, string: String) {
        if (string.length < MAX_LOG_LENGTH) {
            emitOne(severity, tag, string)
            return
        }

        // Split by line, then ensure each line can fit into Log's maximum length.
        var index = 0
        val length = string.length

        while (index < length) {
            var newline = string.indexOf('\n', index)
            newline = if (newline != -1) newline else length

            do {
                val end = newline.coerceAtMost(index + MAX_LOG_LENGTH)
                emitOne(severity, tag, string.substring(index, end))
                index = end
            } while (index < newline)

            index++
        }
    }

    private fun emitOne(severity: Int, tag: String?, line: String) {
        if (severity == Log.ASSERT) {
            Log.wtf(tag, line)
        } else {
            Log.println(severity, tag, line)
        }
    }

    private fun recordError(message: String, rawTag: String) {
        val entry = HatchetError(
            timestamp = System.currentTimeMillis(),
            tag = rawTag,
            thread = Thread.currentThread().name,
            message = message,
        )
        synchronized(errorQueueLock) {
            errorQueue.addLast(entry)
            if (errorQueue.size > MAX_RECENT_ERRORS) errorQueue.removeFirst()
        }
    }

    // Per-thread cache of the padded/ellipsized name so we only rebuild when the thread is
    // renamed (e.g. coroutine dispatchers reusing pool threads with different names).
    private val cachedThreadName = ThreadLocal<Pair<String, String>>()

    private fun currentFormattedThreadName(): String {
        val current = Thread.currentThread().name
        val cached = cachedThreadName.get()
        if (cached != null && cached.first == current) return cached.second
        val formatted = formatThreadName(current)
        cachedThreadName.set(current to formatted)
        return formatted
    }

    private fun formatThreadName(name: String): String = formatFixedWidth(name, THREAD_NAME_WIDTH)

    private fun formatFixedWidth(text: String, width: Int): String = when {
        text.length == width -> text

        text.length < width -> text.padEnd(width)

        else -> {
            val keep = width - ELLIPSIS.length
            val head = (keep + 1) / 2
            val tail = keep / 2
            text.substring(0, head) + ELLIPSIS + text.substring(text.length - tail)
        }
    }

    private val fqcnIgnore = listOf(
        Hatchet::class.java.name,
        AndroidHatchet::class.java.name,
    )

    // Per-className cache of (raw, formatted) tags. `raw` is the bare class name with any
    // anonymous-class suffix stripped — fed into [recordError] so the queue stays dense.
    // `formatted` is `raw` pad/center-ellipsized to a fixed 16-char width for logcat alignment.
    private val cachedTag = ConcurrentHashMap<String, TagPair>()

    private data class TagPair(val raw: String, val formatted: String)

    private fun resolveTag(element: StackTraceElement): TagPair =
        cachedTag.getOrPut(element.className) {
            var raw = element.className.substringAfterLast('.')
            val m = ANONYMOUS_CLASS.matcher(raw)
            if (m.find()) {
                raw = m.replaceAll("")
            }
            TagPair(raw = raw, formatted = formatFixedWidth(raw, TAG_WIDTH))
        }

    companion object {
        private const val MAX_LOG_LENGTH = 4000
        private const val TAG_WIDTH = 16
        private const val THREAD_NAME_WIDTH = 16
        private const val MAX_RECENT_ERRORS = 16
        private const val ELLIPSIS = "..."
        private const val FALLBACK_TAG_RAW = "Chipbox"
        private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
    }
}
