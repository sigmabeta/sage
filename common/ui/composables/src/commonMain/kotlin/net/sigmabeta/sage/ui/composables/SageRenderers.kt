package net.sigmabeta.sage.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.ui.StringProvider

/**
 * One renderer, with the same signature [net.sigmabeta.sage.ui.composables.ListModel.Content] (and
 * `ListScreen`/`GridScreen`'s `itemContent`) takes.
 */
typealias ListModelContent =
    @Composable (model: ListModel, sink: ActionSink, debug: Boolean, modifier: Modifier, padding: PaddingValues) -> Unit

/**
 * The tunable look of the built-in renderers.
 *
 * Each token has a default that reproduces the renderers' historical pixels exactly, so an app that
 * makes no change keeps the SAGE default. Tune them through [SageRenderers].
 */
@Immutable
data class SageRendererStyle(
    /** [net.sigmabeta.sage.ui.composables.GridImage]'s cell corner (its `ElevatedRoundRect` radius). */
    val gridCellCornerRadius: Dp = 16.dp,

    /** [net.sigmabeta.sage.ui.composables.WideItem]'s thumbnail corner and shared-transition clip. */
    val wideItemCornerRadius: Dp = 8.dp,

    /** The hero's corner: the shape every destination's artwork returns to. */
    val heroCornerRadius: Dp = 16.dp,

    /** The vertical rhythm between stacked rows (loading cards, wide items). */
    val itemVerticalSpacing: Dp = 4.dp,

    /** The shadow elevation under [net.sigmabeta.sage.ui.composables.WideItem] and its placeholder. */
    val cardElevation: Dp = 4.dp,
)

/**
 * The single customisation point for SAGE's shared renderer suite.
 *
 * Install it (and the [StringProvider] the renderers read accessibility labels from) with
 * [ProvideSageRenderers], which publishes both locals at once.
 */
@Immutable
class SageRenderers(
    /** The [SageRendererStyle] the built-in renderers read their look from. */
    val style: SageRendererStyle = SageRendererStyle(),

    /**
     * App renderers, consulted before the built-ins. Return `null` to fall through. Use it both for
     * app-owned [ListModel] subclasses and to replace a built-in renderer.
     */
    val contentFor: (ListModel) -> ListModelContent? = { null },

    /**
     * Inspection-mode stand-in art (see the renderer suite's `FakeImage`). `null` = SAGE's default
     * deterministic gradient seeded by the source.
     */
    val previewImage: (@Composable (sourceInfo: SourceInfo, modifier: Modifier) -> Unit)? = null,
)

/** The active [SageRenderers], read by [net.sigmabeta.sage.ui.composables.ListModel.Content]. */
val LocalSageRenderers = staticCompositionLocalOf { SageRenderers() }

/**
 * Installs both locals the renderers read: [LocalSageStringProvider] from [stringProvider] and
 * [LocalSageRenderers] from [renderers].
 */
@Composable
fun ProvideSageRenderers(
    stringProvider: StringProvider,
    renderers: SageRenderers = SageRenderers(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalSageStringProvider provides stringProvider,
        LocalSageRenderers provides renderers,
        content = content,
    )
}
