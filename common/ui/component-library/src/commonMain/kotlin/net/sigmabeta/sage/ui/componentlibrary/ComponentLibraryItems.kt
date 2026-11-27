@file:Suppress("MagicNumber", "TooManyFunctions")

package net.sigmabeta.sage.ui.componentlibrary

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.CheckableListModel
import net.sigmabeta.sage.components.CollapsibleDetailsListModel
import net.sigmabeta.sage.components.ConfirmationListModel
import net.sigmabeta.sage.components.CtaListModel
import net.sigmabeta.sage.components.DropdownSettingListModel
import net.sigmabeta.sage.components.EditTextListModel
import net.sigmabeta.sage.components.EmptyStateListModel
import net.sigmabeta.sage.components.ErrorStateListModel
import net.sigmabeta.sage.components.GridImageListModel
import net.sigmabeta.sage.components.HeroImageListModel
import net.sigmabeta.sage.components.HorizontalScrollerListModel
import net.sigmabeta.sage.components.IconNameCaptionListModel
import net.sigmabeta.sage.components.IconNameListModel
import net.sigmabeta.sage.components.ImageNameCaptionListModel
import net.sigmabeta.sage.components.ImageNameListModel
import net.sigmabeta.sage.components.LabelRatingStarListModel
import net.sigmabeta.sage.components.LabelValueListModel
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.components.LoadingItemListModel
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.NameCaptionCheckboxListModel
import net.sigmabeta.sage.components.NameCaptionListModel
import net.sigmabeta.sage.components.NameCaptionValueListModel
import net.sigmabeta.sage.components.NotifListModel
import net.sigmabeta.sage.components.SearchResultListModel
import net.sigmabeta.sage.components.SectionHeaderListModel
import net.sigmabeta.sage.components.SectionListModel
import net.sigmabeta.sage.components.SingleTextListModel
import net.sigmabeta.sage.components.SmallTextListModel
import net.sigmabeta.sage.components.SubsectionHeaderListModel
import net.sigmabeta.sage.components.SubsectionListModel
import net.sigmabeta.sage.components.WideItemListModel
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.composables.DismissibleListModel
import net.sigmabeta.sage.ui.composables.DraggableListModel

// Labels are plain English: this is a debug-only developer tool, so it has no string ids.

/**
 * The whole gallery for [config]: a Controls section, the showcase for [ComponentLibraryConfig.layout],
 * a States section, and, when [extraSamples] is non-empty, an "App rows" section with those models
 * unchanged (the app renders them through its own `contentFor`).
 *
 * Every returned `dataId` is unique, including nested children, as long as [extraSamples] do not
 * collide with the catalog's.
 */
fun componentLibraryItems(
    config: ComponentLibraryConfig,
    extraSamples: List<ListModel> = emptyList(),
): List<ListModel> = buildList {
    addAll(controls(config))
    addAll(
        when (config.layout) {
            ComponentLibraryLayout.LIST -> listItems(config)
            ComponentLibraryLayout.GRID -> gridItems(config.content)
            ComponentLibraryLayout.COLUMNS -> columnItems(config.content)
        },
    )
    addAll(stateItems())
    if (extraSamples.isNotEmpty()) {
        add(SectionHeaderListModel("App rows"))
        addAll(extraSamples)
    }
}

private val CORNER_OPTIONS = listOf(0, 4, 8, 12, 16, 24, 32)
private val SPACING_OPTIONS = listOf(0, 2, 4, 8, 12, 16)
private val ELEVATION_OPTIONS = listOf(0, 1, 2, 4, 8, 12)

private data class StyleControl(val token: StyleToken, val label: String, val options: List<Int>)

private val STYLE_CONTROLS = listOf(
    StyleControl(StyleToken.GRID_CELL_CORNER, "Grid cell corner", CORNER_OPTIONS),
    StyleControl(StyleToken.WIDE_ITEM_CORNER, "Wide item corner", CORNER_OPTIONS),
    StyleControl(StyleToken.HERO_CORNER, "Hero corner", CORNER_OPTIONS),
    StyleControl(StyleToken.ITEM_SPACING, "Item spacing", SPACING_OPTIONS),
    StyleControl(StyleToken.CARD_ELEVATION, "Card elevation", ELEVATION_OPTIONS),
)

private fun controls(config: ComponentLibraryConfig): List<ListModel> = buildList {
    add(SectionHeaderListModel("Controls"))

    val layouts = ComponentLibraryLayout.entries
    add(
        DropdownSettingListModel.ofLabels(
            settingId = LAYOUT_DROPDOWN_ID,
            name = "Layout: ${config.layout.label()}",
            selectedPosition = config.layout.ordinal,
            labels = layouts.map { it.label() }.toImmutableList(),
            expanded = config.expandedDropdownId == LAYOUT_DROPDOWN_ID,
            onExpandClicked = ComponentLibraryAction.ToggleDropdown(LAYOUT_DROPDOWN_ID),
            onNewOptionSelected = { ComponentLibraryAction.SetLayout(layouts[it]) },
        ),
    )

    STYLE_CONTROLS.forEach { control ->
        val id = "$STYLE_DROPDOWN_PREFIX${control.token.name}"
        val current = config.style.valueOf(control.token)
        add(
            DropdownSettingListModel.ofLabels(
                settingId = id,
                name = "${control.label}: $current dp",
                selectedPosition = control.options.indexOf(current).coerceAtLeast(0),
                labels = control.options.map { "$it dp" }.toImmutableList(),
                expanded = config.expandedDropdownId == id,
                onExpandClicked = ComponentLibraryAction.ToggleDropdown(id),
                onNewOptionSelected = { ComponentLibraryAction.SetStyle(control.token, control.options[it]) },
            ),
        )
    }

    add(CtaListModel(icon = Icon.Warning, name = "Reset style", clickAction = ComponentLibraryAction.ResetStyle))
}

private fun ComponentLibraryLayout.label() = when (this) {
    ComponentLibraryLayout.LIST -> "List"
    ComponentLibraryLayout.GRID -> "Grid"
    ComponentLibraryLayout.COLUMNS -> "Columns"
}

private fun stateItems(): List<ListModel> = buildList {
    add(SectionHeaderListModel("States"))
    LoadingType.entries.forEach { type ->
        add(LoadingItemListModel(loadingType = type, loadOperationName = "gallery-${type.name}", loadPositionOffset = 0))
    }
    add(EmptyStateListModel(icon = Icon.Warning, explanation = "Nothing to show here", showCrossOut = true))
    add(
        ErrorStateListModel(
            failedOperationName = "gallery",
            errorString = "Sample error from the component gallery",
            error = IllegalStateException("Sample error from the component gallery"),
        ),
    )
}

// Full-width, single-column showcase: one of (most) every component, plus a horizontal
// scroller of grid items at the end.
private fun listItems(config: ComponentLibraryConfig): List<ListModel> = buildList {
    val content = config.content
    add(SectionHeaderListModel("HeroImage / ImageName*"))
    add(
        HeroImageListModel(
            sourceInfo = SourceInfo("$IMAGE_SEED/hero"),
            contentDescription = content.title(0),
            imagePlaceholder = Icon.Album,
            clickAction = SageAction.Noop,
        ),
    )
    add(
        ImageNameCaptionListModel(
            dataId = id(0),
            name = content.name(0),
            caption = content.caption(0),
            sourceInfo = SourceInfo("$IMAGE_SEED/inc/0"),
            imagePlaceholder = Icon.Person,
            clickAction = SageAction.Noop,
        ),
    )
    add(
        ImageNameListModel(
            dataId = id(1),
            name = content.name(1),
            sourceInfo = SourceInfo("$IMAGE_SEED/in/1"),
            imagePlaceholder = Icon.Person,
            clickAction = SageAction.Noop,
        ),
    )
    add(
        SearchResultListModel(
            dataId = id(17),
            name = content.name(5),
            caption = content.caption(7),
            sourceInfo = SourceInfo("$IMAGE_SEED/sr/17"),
            imagePlaceholder = Icon.Person,
            clickAction = SageAction.Noop,
        ),
    )

    add(SectionHeaderListModel("Icon / text rows"))
    add(IconNameCaptionListModel(id(2), content.title(1), content.caption(1), Icon.MusicNote, SageAction.Noop))
    add(IconNameListModel(id(3), content.title(2), Icon.Tag, SageAction.Noop))
    add(NameCaptionListModel(id(4), content.title(3), content.caption(2), SageAction.Noop))
    add(NameCaptionValueListModel(id(5), content.title(4), content.caption(3), "42", SageAction.Noop))
    add(LabelValueListModel(label = content.title(5), value = content.name(2), clickAction = SageAction.Noop, dataId = id(10)))
    add(SingleTextListModel(name = content.title(6), clickAction = SageAction.Noop, dataId = id(11)))
    add(SmallTextListModel(name = content.title(7), clickAction = SageAction.Noop, dataId = id(18)))
    add(
        CollapsibleDetailsListModel(
            dataId = id(16),
            title = content.title(8),
            detailItems = List(COLLAPSIBLE_DETAIL_COUNT) { content.caption(it) }.toImmutableList(),
            initiallyCollapsed = true,
        ),
    )

    add(SectionHeaderListModel("Setting controls"))
    add(
        CheckableListModel(
            settingId = "cl-check",
            name = content.title(7),
            checked = config.checked,
            clickAction = ComponentLibraryAction.ToggleChecked,
        ),
    )
    add(
        NameCaptionCheckboxListModel(
            settingId = "cl-check-caption",
            name = content.title(9),
            caption = content.caption(8),
            checked = false,
            clickAction = SageAction.Noop,
        ),
    )
    add(LabelRatingStarListModel(label = content.title(8), value = STAR_RATING, clickAction = SageAction.Noop, dataId = id(12)))
    add(
        DropdownSettingListModel.ofLabels(
            settingId = DROPDOWN_SETTING_ID,
            name = content.title(9),
            selectedPosition = 0,
            labels = content.titles.take(DROPDOWN_OPTION_COUNT).toImmutableList(),
            expanded = config.expandedDropdownId == DROPDOWN_SETTING_ID,
            onExpandClicked = ComponentLibraryAction.ToggleDropdown(DROPDOWN_SETTING_ID),
        ),
    )
    add(CtaListModel(icon = Icon.Search, name = content.title(10), clickAction = SageAction.Noop, dataId = id(13)))
    add(EditTextListModel(id = id(14), header = content.title(11), hint = content.title(12), allowEmpty = false))
    add(ConfirmationListModel(id = id(15), header = content.title(0), bodyText = content.caption(0), confirmLabel = content.title(1)))

    add(SectionHeaderListModel("WideItem / Notif"))
    add(
        WideItemListModel(
            dataId = id(6),
            name = content.name(3),
            sourceInfo = SourceInfo("$IMAGE_SEED/wide/0"),
            imagePlaceholder = Icon.Person,
            clickAction = SageAction.Noop,
        ),
    )
    add(notif(id(7), content.title(11), content.caption(4), "OK", isError = false))
    add(notif(id(8), content.name(4), content.caption(5), "Retry", isError = true))

    add(SectionHeaderListModel("Wrappers / sections"))
    add(DismissibleListModel(content = SingleTextListModel(content.title(10), SageAction.Noop, dataId = id(21)), dismissAction = SageAction.Noop))
    add(DraggableListModel(content = SingleTextListModel(content.title(11), SageAction.Noop, dataId = id(22))))
    add(
        SectionListModel(
            dataId = id(20),
            dontUnroll = true,
            sectionItems = persistentListOf(
                SingleTextListModel(content.title(2), SageAction.Noop, dataId = id(23)),
                SingleTextListModel(content.title(3), SageAction.Noop, dataId = id(24)),
            ),
        ),
    )

    add(SectionHeaderListModel("HorizontalScroller (grid items)"))
    add(
        HorizontalScrollerListModel(
            dataId = id(9),
            scrollingItems = gridCells(content, "$IMAGE_SEED/scroll", SCROLLER_CELL_COUNT, Icon.Album),
        ),
    )

    add(EmptyStateListModel(icon = Icon.Warning, explanation = content.caption(6), showCrossOut = false))
}

// Multi-column grid of square cells, with a full-width header and a full-width notif mixed in to
// show that COLUMNS_ALL items still span the whole row inside the grid.
private fun gridItems(content: ComponentLibraryContent): List<ListModel> = buildList {
    add(SectionHeaderListModel("Grid cells"))
    addAll(gridCells(content, "$IMAGE_SEED/grid", GRID_CELL_COUNT, Icon.Album))
    add(notif(id(0), content.title(0), content.caption(0), "OK", isError = false))
    addAll(gridCells(content, "$IMAGE_SEED/grid2", GRID_CELL_COUNT, Icon.Person))
}

// Staggered layout matching the detail screens (one lane on a phone, two when wider): a full-width
// header, a Subsection (itself a 2-up flow of grid items), a horizontal scroller, then wide items
// and grid cells that tile into the available lanes.
private fun columnItems(content: ComponentLibraryContent): List<ListModel> = buildList {
    add(SectionHeaderListModel("Detail-style columns"))
    add(CtaListModel(icon = Icon.MusicNote, name = content.title(0), clickAction = SageAction.Noop, dataId = id(2)))
    add(
        SubsectionListModel(
            id = id(0),
            titleModel = SubsectionHeaderListModel(content.title(1)),
            children = gridCells(content, "$IMAGE_SEED/sub", SUBSECTION_CELL_COUNT, Icon.Person),
        ),
    )
    add(
        HorizontalScrollerListModel(
            dataId = id(1),
            scrollingItems = gridCells(content, "$IMAGE_SEED/colscroll", SCROLLER_CELL_COUNT, Icon.Album),
        ),
    )
    repeat(COLUMN_WIDE_COUNT) { i ->
        add(
            WideItemListModel(
                dataId = id(WIDE_ID_BASE + i),
                name = content.name(i),
                sourceInfo = SourceInfo("$IMAGE_SEED/colwide/$i"),
                imagePlaceholder = Icon.Person,
                clickAction = SageAction.Noop,
            ),
        )
    }
    addAll(gridCells(content, "$IMAGE_SEED/colgrid", GRID_CELL_COUNT, Icon.Album))
}

private fun notif(dataId: Long, title: String, description: String, actionLabel: String, isError: Boolean) =
    NotifListModel(
        dataId = dataId,
        title = title,
        description = description,
        actionLabel = actionLabel,
        action = SageAction.Noop,
        isError = isError,
    )

private fun gridCells(
    content: ComponentLibraryContent,
    seed: String,
    count: Int,
    placeholder: Icon,
): ImmutableList<ListModel> = List(count) { i ->
    GridImageListModel(
        dataId = "$seed/$i".hashCode().toLong(),
        name = content.title(i),
        sourceInfo = SourceInfo("$seed/$i"),
        imagePlaceholder = placeholder,
        clickAction = SageAction.Noop,
    )
}.toImmutableList()

// Stable, locally-unique dataIds for the explicitly-keyed rows. Offset well clear of the
// hash-derived ids the image cells use.
private fun id(offset: Int): Long = ID_BASE + offset

private const val LAYOUT_DROPDOWN_ID = "cl-layout"
private const val STYLE_DROPDOWN_PREFIX = "cl-style-"
private const val DROPDOWN_OPTION_COUNT = 6
private const val DROPDOWN_SETTING_ID = "cl-dropdown"
private const val SCROLLER_CELL_COUNT = 8
private const val GRID_CELL_COUNT = 6
private const val SUBSECTION_CELL_COUNT = 4
private const val COLUMN_WIDE_COUNT = 4
private const val COLLAPSIBLE_DETAIL_COUNT = 3
private const val STAR_RATING = 3
private const val ID_BASE = 1_000_000L
private const val WIDE_ID_BASE = 100
private const val IMAGE_SEED = "component-library"
