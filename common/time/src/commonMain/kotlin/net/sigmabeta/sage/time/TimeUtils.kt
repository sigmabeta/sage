package net.sigmabeta.sage.time

import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

object TimeUtils {
    fun calculateAgeOf(instant: Instant): Duration = Clock.System.now() - instant
}
