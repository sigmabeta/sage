package net.sigmabeta.sage.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import net.sigmabeta.sage.ui.composables.previews.CoverArtConstants
import net.sigmabeta.sage.ui.composables.subs.CrossfadeImage
import net.sigmabeta.sage.ui.composables.subs.ElevatedRoundRect
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.components.HeroImageListModel
import net.sigmabeta.sage.images.SourceInfo

@Composable
fun HeroImage(
    model: HeroImageListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    // IGDB covers are 3:4 portrait. Fill the available width and let the height follow the
    // ratio — same width-driven sizing as the grid tiles and the BIG_IMAGE loading placeholder.
    val style = LocalSageRenderers.current.style
    ElevatedRoundRect(
        cornerRadius = style.heroCornerRadius,
        modifier = modifier
            .padding(padding)
            .fillMaxWidth()
            .aspectRatio(CoverArtConstants.ASPECT_RATIO)
            .clickable(onClick = { actionSink.sendAction(model.clickAction) }),
    ) {
        CrossfadeImage(
            sourceInfo = model.sourceInfo,
            imagePlaceholder = model.imagePlaceholder,
            contentDescription = model.contentDescription,
            // The same artwork as the row that pushed this detail, so the two are one shared
            // element across the push transition.
            modifier = Modifier.fillMaxSize().sharedImageElement(
                sourceInfo = model.sourceInfo,
                corner = heroArtworkCorner(),
                arrivingFrom = LocalArtworkHandoff.current?.corner,
            ),
            // ...and the row's already-decoded bitmap is drawn while this (larger) request loads,
            // so the shared element shows artwork rather than a placeholder icon on its way in.
            placeholderSourceInfo = LocalArtworkHandoff.current?.url?.let(::SourceInfo),
            // A hero must never flash the loader's stand-in: it paints the cached artwork when
            // there is one and an empty frame otherwise.
            placeholderIcon = false,
            loadOriginalSize = true,
        )
    }
}
