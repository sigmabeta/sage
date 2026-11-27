package net.sigmabeta.sage.ui.componentlibrary

import net.sigmabeta.sage.components.CheckableListModel
import net.sigmabeta.sage.components.DropdownSettingListModel
import net.sigmabeta.sage.components.HorizontalScrollerListModel
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.components.LoadingItemListModel
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.SingleTextListModel
import net.sigmabeta.sage.components.SectionListModel
import net.sigmabeta.sage.components.SubsectionListModel
import net.sigmabeta.sage.ui.composables.DismissibleListModel
import net.sigmabeta.sage.ui.composables.DraggableListModel
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ComponentLibraryItemsTest {
    private val extras = listOf<ListModel>(
        SingleTextListModel(name = "App row one", clickAction = net.sigmabeta.sage.appcomm.SageAction.Noop, dataId = 42L),
    )

    private fun ListModel.walk(): List<ListModel> = listOf(this) + when (this) {
        is HorizontalScrollerListModel -> scrollingItems.flatMap { it.walk() }
        is SubsectionListModel -> titleModel.walk() + children.flatMap { it.walk() }
        is SectionListModel -> sectionItems.flatMap { it.walk() }
        is DismissibleListModel -> content.walk()
        is DraggableListModel -> content.walk()
        else -> emptyList()
    }

    // The ids that must be unique. Dismissible/Draggable wrappers delegate dataId to their content
    // by design, and a Subsection's dataId is its header's, so neither is counted twice.
    private fun ListModel.ids(): List<Long> = when (this) {
        is HorizontalScrollerListModel -> listOf(dataId) + scrollingItems.flatMap { it.ids() }
        is SubsectionListModel -> listOf(dataId) + children.flatMap { it.ids() }
        is SectionListModel -> listOf(dataId) + sectionItems.flatMap { it.ids() }
        is DismissibleListModel -> content.ids()
        is DraggableListModel -> content.ids()
        else -> listOf(dataId)
    }

    private fun items(layout: ComponentLibraryLayout, withExtras: Boolean) =
        componentLibraryItems(ComponentLibraryConfig(layout = layout), if (withExtras) extras else emptyList())

    @Test
    fun `no duplicate dataIds in any layout`() {
        ComponentLibraryLayout.entries.forEach { layout ->
            listOf(false, true).forEach { withExtras ->
                val ids = items(layout, withExtras).flatMap { it.ids() }
                val dupes = ids.groupBy { it }.filterValues { it.size > 1 }.keys
                assertTrue(dupes.isEmpty(), "$layout extras=$withExtras duplicate ids: $dupes")
            }
        }
    }

    @Test
    fun `top level ids stay unique with an open dropdown`() {
        ComponentLibraryLayout.entries.forEach { layout ->
            val config = ComponentLibraryConfig(layout = layout, expandedDropdownId = "cl-dropdown")
            val ids = componentLibraryItems(config).map { it.dataId }
            assertEquals(ids.size, ids.toSet().size)
        }
    }

    @Test
    fun `extras are appended unchanged`() {
        val result = items(ComponentLibraryLayout.LIST, withExtras = true)
        assertEquals(extras, result.takeLast(extras.size))
        assertTrue(items(ComponentLibraryLayout.LIST, withExtras = false).none { it in extras })
    }

    @Test
    fun `controls reflect config`() {
        val config = ComponentLibraryConfig(layout = ComponentLibraryLayout.GRID, expandedDropdownId = "cl-layout")
        val dropdowns = componentLibraryItems(config).filterIsInstance<DropdownSettingListModel>()
        assertEquals(1 + StyleToken.entries.size, dropdowns.count { it.settingId.startsWith("cl-layout") || it.settingId.startsWith("cl-style-") })
        assertTrue(dropdowns.single { it.settingId == "cl-layout" }.expanded)
        assertTrue(dropdowns.filter { it.settingId != "cl-layout" }.none { it.expanded })
    }

    @Test
    fun `list layout checkbox follows config`() {
        val config = ComponentLibraryConfig(checked = false)
        val check = componentLibraryItems(config).filterIsInstance<CheckableListModel>().single()
        assertEquals(false, check.checked)
        assertEquals(ComponentLibraryAction.ToggleChecked, check.clickAction)
    }

    @Test
    fun `every built-in model type and loading type appears across layouts`() {
        val seen: Set<KClass<*>> = ComponentLibraryLayout.entries
            .flatMap { items(it, withExtras = false) }
            .flatMap { it.walk() }
            .map { it::class }
            .toSet()
        val missing = BUILT_IN_TYPES - seen
        assertTrue(missing.isEmpty(), "Built-in types missing from the gallery: $missing")

        val loadingSeen = ComponentLibraryLayout.entries
            .flatMap { items(it, withExtras = false) }
            .filterIsInstance<LoadingItemListModel>()
            .map { it.loadingType }
            .toSet()
        assertEquals(LoadingType.entries.toSet(), loadingSeen)
    }

    private companion object {
        // Mirrors the `is` branches of ComposableMapping.kt's BuiltInContent, minus EXEMPT below.
        val BUILT_IN_TYPES: Set<KClass<*>> = setOf(
            CheckableListModel::class,
            net.sigmabeta.sage.components.CollapsibleDetailsListModel::class,
            net.sigmabeta.sage.components.ConfirmationListModel::class,
            net.sigmabeta.sage.components.CtaListModel::class,
            DropdownSettingListModel::class,
            net.sigmabeta.sage.components.EditTextListModel::class,
            net.sigmabeta.sage.components.EmptyStateListModel::class,
            net.sigmabeta.sage.components.ErrorStateListModel::class,
            net.sigmabeta.sage.components.HeroImageListModel::class,
            HorizontalScrollerListModel::class,
            net.sigmabeta.sage.components.IconNameCaptionListModel::class,
            net.sigmabeta.sage.components.IconNameListModel::class,
            net.sigmabeta.sage.components.ImageNameCaptionListModel::class,
            net.sigmabeta.sage.components.ImageNameListModel::class,
            net.sigmabeta.sage.components.LabelRatingStarListModel::class,
            net.sigmabeta.sage.components.LabelValueListModel::class,
            net.sigmabeta.sage.components.NameCaptionListModel::class,
            net.sigmabeta.sage.components.NameCaptionCheckboxListModel::class,
            net.sigmabeta.sage.components.NameCaptionValueListModel::class,
            net.sigmabeta.sage.components.NotifListModel::class,
            net.sigmabeta.sage.components.SearchResultListModel::class,
            SectionListModel::class,
            net.sigmabeta.sage.components.SectionHeaderListModel::class,
            SingleTextListModel::class,
            net.sigmabeta.sage.components.SmallTextListModel::class,
            net.sigmabeta.sage.components.GridImageListModel::class,
            net.sigmabeta.sage.components.SubsectionHeaderListModel::class,
            SubsectionListModel::class,
            WideItemListModelRef::class,
            DraggableListModel::class,
            DismissibleListModel::class,
            LoadingItemListModel::class,
        )

        // Exempt from the gallery, each with a reason:
        // - NoopListModel: a filtered-out placeholder; its dataId/columns throw by design.
        // - SearchHistoryListModel: only meaningful inside the search screen's history state.
        // - SheetPageListModel, SheetPageCardListModel, ZoomableSheetPageListModel, SinglePageListModel:
        //   PDF page models, only meaningful inside a document viewer with real page sources.
        // - NetworkRefreshingListModel: not rendered by BuiltInContent (falls to the blank Spacer).
    }
}

private typealias WideItemListModelRef = net.sigmabeta.sage.components.WideItemListModel
