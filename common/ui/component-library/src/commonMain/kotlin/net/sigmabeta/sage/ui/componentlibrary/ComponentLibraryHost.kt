package net.sigmabeta.sage.ui.componentlibrary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.ui.composables.LocalSageRenderers
import net.sigmabeta.sage.ui.composables.SageRendererStyle
import net.sigmabeta.sage.ui.composables.SageRenderers

/** The [SageRendererStyle] equivalent of this style. */
fun ComponentLibraryStyle.toRendererStyle(): SageRendererStyle = SageRendererStyle(
    gridCellCornerRadius = gridCellCornerRadiusDp.dp,
    wideItemCornerRadius = wideItemCornerRadiusDp.dp,
    heroCornerRadius = heroCornerRadiusDp.dp,
    itemVerticalSpacing = itemVerticalSpacingDp.dp,
    cardElevation = cardElevationDp.dp,
)

/**
 * Wrap the gallery screen's list in this, passing the rendered `ListStateActual.listItems`: the
 * style is read back from the list's invisible carrier row ([componentLibraryStyleOf]), so no typed
 * state flow is needed. Otherwise identical to the [ComponentLibraryStyle] overload.
 */
@Composable
fun ComponentLibraryHost(items: List<ListModel>, content: @Composable () -> Unit) {
    ComponentLibraryHost(componentLibraryStyleOf(items), content)
}

/**
 * Wrap the gallery screen's list in this when the app holds the config directly: applies [style] to `LocalSageRenderers` (copying the
 * app's renderers so its `contentFor` / `previewImage` survive) and forces `LocalInspectionMode`
 * to true so the catalog's synthetic `SourceInfo`s render preview art instead of failing image
 * loads.
 */
@Composable
fun ComponentLibraryHost(style: ComponentLibraryStyle, content: @Composable () -> Unit) {
    val current = LocalSageRenderers.current
    val renderers = remember(current, style) {
        SageRenderers(
            style = style.toRendererStyle(),
            contentFor = current.contentFor,
            previewImage = current.previewImage,
        )
    }
    CompositionLocalProvider(
        LocalSageRenderers provides renderers,
        LocalInspectionMode provides true,
        content = content,
    )
}
