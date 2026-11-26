package net.sigmabeta.sage.ui.composables.subs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.ui.composables.LocalSageRenderers
import kotlin.random.Random

/**
 * Inspection-only stand-in for a real Coil-loaded image.
 *
 * Reached via [CrossfadeImage]'s `forceGenBitmap` branch (defaulting to
 * [androidx.compose.ui.platform.LocalInspectionMode.current]), so a `@Preview` / Paparazzi render
 * shows meaningful artwork instead of the loading/error placeholder Coil falls back to when it can't
 * fetch in a preview.
 *
 * The app decides the art through [LocalSageRenderers]'s `previewImage` hook; when it leaves the
 * default `null`, SAGE draws a deterministic gradient seeded by the source.
 */
@Composable
internal fun FakeImage(sourceInfo: SourceInfo, modifier: Modifier) {
    val previewImage = LocalSageRenderers.current.previewImage
    if (previewImage != null) {
        previewImage(sourceInfo, modifier)
        return
    }

    // Only hit under LocalInspectionMode — never at app runtime, where CrossfadeImage takes the real
    // Coil path.
    val brush = remember(sourceInfo.info) {
        val rng = Random(sourceInfo.info.hashCode())
        Brush.linearGradient(
            listOf(Color(rng.nextInt()), Color(rng.nextInt()), Color(rng.nextInt())),
        )
    }
    Box(modifier.background(brush))
}
