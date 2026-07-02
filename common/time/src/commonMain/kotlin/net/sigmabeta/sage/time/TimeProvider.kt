package net.sigmabeta.sage.time

import kotlinx.datetime.LocalDate
import kotlin.time.Instant

/**
 * Multiplatform time/date access. Replaces the old threeten-based `ThreeTenTime`; the platform
 * implementation supplies the (locale-aware) formatting, while the shared code deals only in
 * kotlinx-datetime / kotlin.time types.
 */
interface TimeProvider {
    fun now(): Instant

    fun localDateFromString(date: String): LocalDate?

    fun longDateTextFromMillis(timestamp: Long): String?

    fun longDateTimeText(instant: Instant): String
}
