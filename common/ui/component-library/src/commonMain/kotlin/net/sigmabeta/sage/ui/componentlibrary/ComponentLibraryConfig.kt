package net.sigmabeta.sage.ui.componentlibrary

import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.list.ColumnType

/** Which showcase the gallery renders. */
enum class ComponentLibraryLayout {
    /** One full-width column: one of (most) every component. */
    LIST,

    /** A multi-column grid of square cells. */
    GRID,

    /** The detail screens' staggered layout. */
    COLUMNS;

    /** The [ColumnType] the gallery's list state should report for this layout. */
    val columnType: ColumnType
        get() = when (this) {
            LIST -> ColumnType.One
            GRID -> ColumnType.Regular(GRID_CELL_WIDTH_DP)
            COLUMNS -> ColumnType.Staggered(COLUMN_WIDTH_DP, allowHorizScroller = false)
        }

    private companion object {
        const val GRID_CELL_WIDTH_DP = 160

        // The width the detail screens use: one column on a phone, two when wider.
        const val COLUMN_WIDTH_DP = 320
    }
}

/**
 * The five `SageRendererStyle` tokens in whole dp, kept Compose-free so feature modules can hold
 * them in state. Defaults equal `SageRendererStyle()`; see [toRendererStyle].
 */
data class ComponentLibraryStyle(
    /** Corner radius of a grid cell. */
    val gridCellCornerRadiusDp: Int = 16,
    /** Corner radius of a wide item's thumbnail. */
    val wideItemCornerRadiusDp: Int = 8,
    /** Corner radius of the hero image. */
    val heroCornerRadiusDp: Int = 16,
    /** Vertical spacing between stacked rows. */
    val itemVerticalSpacingDp: Int = 4,
    /** Shadow elevation under wide items. */
    val cardElevationDp: Int = 4,
)

/** One tweakable [ComponentLibraryStyle] field. */
enum class StyleToken {
    /** [ComponentLibraryStyle.gridCellCornerRadiusDp]. */
    GRID_CELL_CORNER,

    /** [ComponentLibraryStyle.wideItemCornerRadiusDp]. */
    WIDE_ITEM_CORNER,

    /** [ComponentLibraryStyle.heroCornerRadiusDp]. */
    HERO_CORNER,

    /** [ComponentLibraryStyle.itemVerticalSpacingDp]. */
    ITEM_SPACING,

    /** [ComponentLibraryStyle.cardElevationDp]. */
    CARD_ELEVATION,
}

/**
 * Everything the gallery needs to render, held by the app's own state.
 *
 * Defaults to the LIST layout, default style, and seed-0 sample text.
 */
data class ComponentLibraryConfig(
    /** The active showcase. */
    val layout: ComponentLibraryLayout = ComponentLibraryLayout.LIST,
    /** The live style tweaks. */
    val style: ComponentLibraryStyle = ComponentLibraryStyle(),
    /** Pre-generated sample text, held so labels stay stable across recompositions. */
    val content: ComponentLibraryContent = componentLibraryContent(),
    /** The `settingId` of the single expanded dropdown, or null when all are collapsed. */
    val expandedDropdownId: String? = null,
    /** State of the demo checkbox row. */
    val checked: Boolean = true,
)

/** Actions the gallery's own controls send; forward them to [reduce]. */
sealed class ComponentLibraryAction : SageAction() {
    /** Switch the showcase; collapses any open dropdown. */
    data class SetLayout(val layout: ComponentLibraryLayout) : ComponentLibraryAction()

    /** Set one style token to [dp]; collapses any open dropdown. */
    data class SetStyle(val token: StyleToken, val dp: Int) : ComponentLibraryAction()

    /** Restore the default style. */
    data object ResetStyle : ComponentLibraryAction()

    /** Open the dropdown with [dropdownId], or close it if already open. */
    data class ToggleDropdown(val dropdownId: String) : ComponentLibraryAction()

    /** Flip the demo checkbox. */
    data object ToggleChecked : ComponentLibraryAction()
}

/** Pure. Selecting an option (`SetLayout` / `SetStyle`) also collapses the open dropdown. */
fun ComponentLibraryConfig.reduce(action: ComponentLibraryAction): ComponentLibraryConfig = when (action) {
    is ComponentLibraryAction.SetLayout -> copy(layout = action.layout, expandedDropdownId = null)

    is ComponentLibraryAction.SetStyle -> copy(style = style.with(action.token, action.dp), expandedDropdownId = null)

    ComponentLibraryAction.ResetStyle -> copy(style = ComponentLibraryStyle(), expandedDropdownId = null)

    is ComponentLibraryAction.ToggleDropdown -> copy(
        expandedDropdownId = if (expandedDropdownId == action.dropdownId) null else action.dropdownId,
    )

    ComponentLibraryAction.ToggleChecked -> copy(checked = !checked)
}

internal fun ComponentLibraryStyle.valueOf(token: StyleToken): Int = when (token) {
    StyleToken.GRID_CELL_CORNER -> gridCellCornerRadiusDp
    StyleToken.WIDE_ITEM_CORNER -> wideItemCornerRadiusDp
    StyleToken.HERO_CORNER -> heroCornerRadiusDp
    StyleToken.ITEM_SPACING -> itemVerticalSpacingDp
    StyleToken.CARD_ELEVATION -> cardElevationDp
}

private fun ComponentLibraryStyle.with(token: StyleToken, dp: Int): ComponentLibraryStyle = when (token) {
    StyleToken.GRID_CELL_CORNER -> copy(gridCellCornerRadiusDp = dp)
    StyleToken.WIDE_ITEM_CORNER -> copy(wideItemCornerRadiusDp = dp)
    StyleToken.HERO_CORNER -> copy(heroCornerRadiusDp = dp)
    StyleToken.ITEM_SPACING -> copy(itemVerticalSpacingDp = dp)
    StyleToken.CARD_ELEVATION -> copy(cardElevationDp = dp)
}
