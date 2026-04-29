package net.sigmabeta.sage.time

import org.threeten.bp.Duration
import org.threeten.bp.Instant

object TimeUtils {
    fun calculateAgeOf(instant: Instant) = Duration.between(instant, Instant.now())
}
