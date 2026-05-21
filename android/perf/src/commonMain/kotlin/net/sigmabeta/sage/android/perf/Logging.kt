@file:Suppress("MaxLineLength")

package net.sigmabeta.sage.android.perf

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import net.sigmabeta.sage.logging.BasicHatchet
import net.sigmabeta.sage.logging.Hatchet
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.measureTime
import kotlin.time.toDuration

val LocalLogger = staticCompositionLocalOf<Hatchet> { BasicHatchet() }

/**
 * Gates the `measureTime { content() }` wrapping in [WithMeasurementScreen] /
 * [WithMeasurementComponent]. Default `true` so the JVM/desktop dev build gets the same
 * slow-component visibility the Android debug build does. The Android Application sets this
 * to `BuildConfig.DEBUG` at startup so release APKs skip the measurement overhead — pre-KMP
 * this was a compile-time constant via the module's `BuildConfig.DEBUG`; AGP 9's KMP Android
 * Library extension doesn't expose `buildConfig = true`, so it's a runtime toggle now.
 */
var isPerfMeasurementEnabled: Boolean = true

// Hatchet severity ints follow the Android Log.* convention (V=2, D=3, I=4, W=5, E=6).
private const val SEVERITY_INFO = 4
private const val SEVERITY_WARN = 5
private const val SEVERITY_ERROR = 6

private const val THRESHOLD_WARNING_MS_SCREEN_PREVIEW = 1
private const val THRESHOLD_ERROR_MS_SCREEN_PREVIEW = 5

private const val THRESHOLD_WARNING_MS_SCREEN_DEVICE = 7
private const val THRESHOLD_ERROR_MS_SCREEN_DEVICE = 12

private const val THRESHOLD_WARNING_US_COMPONENT_DEVICE = 1500
private const val THRESHOLD_ERROR_US_COMPONENT_DEVICE = 3000

val DURATION_THRESHOLD_WARNING_SCREEN_DEVICE = THRESHOLD_WARNING_MS_SCREEN_DEVICE.toDuration(DurationUnit.MILLISECONDS)
val DURATION_THRESHOLD_ERROR_SCREEN_DEVICE = THRESHOLD_ERROR_MS_SCREEN_DEVICE.toDuration(DurationUnit.MILLISECONDS)

val DURATION_THRESHOLD_WARNING_SCREEN_PREVIEW = THRESHOLD_WARNING_MS_SCREEN_PREVIEW.toDuration(DurationUnit.MILLISECONDS)
val DURATION_THRESHOLD_ERROR_SCREEN_PREVIEW = THRESHOLD_ERROR_MS_SCREEN_PREVIEW.toDuration(DurationUnit.MILLISECONDS)

val DURATION_THRESHOLD_WARNING_COMPONENT_DEVICE = THRESHOLD_WARNING_US_COMPONENT_DEVICE.toDuration(DurationUnit.MICROSECONDS)
val DURATION_THRESHOLD_ERROR_COMPONENT_DEVICE = THRESHOLD_ERROR_US_COMPONENT_DEVICE.toDuration(DurationUnit.MICROSECONDS)

@Composable
fun WithMeasurementScreen(
    title: String,
    warningThreshold: Duration,
    errorThreshold: Duration,
    content: @Composable () -> Unit
) {
    if (!isPerfMeasurementEnabled) {
        content()
        return
    }

    val duration = measureTime {
        content()
    }

    val severity = when {
        duration < warningThreshold -> SEVERITY_INFO
        duration < errorThreshold -> SEVERITY_WARN
        else -> SEVERITY_ERROR
    }

    val hatchet = LocalLogger.current

    hatchet.log(
        severity = severity,
        message = "Composing screen with title: $title took $duration."
    )
}

@Composable
fun WithMeasurementComponent(
    name: String,
    warningThreshold: Duration,
    errorThreshold: Duration,
    content: @Composable () -> Unit
) {
    if (!isPerfMeasurementEnabled) {
        content()
        return
    }

    val duration = measureTime {
        content()
    }

    val severity = when {
        duration < warningThreshold -> return
        duration < errorThreshold -> SEVERITY_WARN
        else -> SEVERITY_ERROR
    }

    val hatchet = LocalLogger.current

    hatchet.log(
        severity = severity,
        message = "Composing component with name: $name took $duration."
    )
}
