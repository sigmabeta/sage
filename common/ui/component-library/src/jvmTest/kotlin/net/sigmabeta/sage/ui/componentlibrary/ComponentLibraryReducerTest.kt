package net.sigmabeta.sage.ui.componentlibrary

import net.sigmabeta.sage.ui.composables.SageRendererStyle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ComponentLibraryReducerTest {
    private val start = ComponentLibraryConfig()

    @Test
    fun `set layout`() {
        ComponentLibraryLayout.entries.forEach {
            assertEquals(it, start.reduce(ComponentLibraryAction.SetLayout(it)).layout)
        }
    }

    @Test
    fun `each style token is set independently`() {
        val s = ComponentLibraryStyle()
        val expected = mapOf(
            StyleToken.GRID_CELL_CORNER to s.copy(gridCellCornerRadiusDp = 3),
            StyleToken.WIDE_ITEM_CORNER to s.copy(wideItemCornerRadiusDp = 3),
            StyleToken.HERO_CORNER to s.copy(heroCornerRadiusDp = 3),
            StyleToken.ITEM_SPACING to s.copy(itemVerticalSpacingDp = 3),
            StyleToken.CARD_ELEVATION to s.copy(cardElevationDp = 3),
        )
        assertEquals(StyleToken.entries.toSet(), expected.keys)
        expected.forEach { (token, style) ->
            assertEquals(style, start.reduce(ComponentLibraryAction.SetStyle(token, 3)).style, token.name)
        }
    }

    @Test
    fun `reset restores default style`() {
        val changed = start.reduce(ComponentLibraryAction.SetStyle(StyleToken.HERO_CORNER, 0))
        assertEquals(ComponentLibraryStyle(), changed.reduce(ComponentLibraryAction.ResetStyle).style)
    }

    @Test
    fun `toggle dropdown opens then closes`() {
        val open = start.reduce(ComponentLibraryAction.ToggleDropdown("a"))
        assertEquals("a", open.expandedDropdownId)
        assertNull(open.reduce(ComponentLibraryAction.ToggleDropdown("a")).expandedDropdownId)
    }

    @Test
    fun `opening another dropdown closes the first`() {
        val open = start.reduce(ComponentLibraryAction.ToggleDropdown("a"))
        assertEquals("b", open.reduce(ComponentLibraryAction.ToggleDropdown("b")).expandedDropdownId)
    }

    @Test
    fun `selecting an option collapses the open dropdown`() {
        val open = start.reduce(ComponentLibraryAction.ToggleDropdown("a"))
        assertNull(open.reduce(ComponentLibraryAction.SetLayout(ComponentLibraryLayout.GRID)).expandedDropdownId)
        assertNull(open.reduce(ComponentLibraryAction.SetStyle(StyleToken.ITEM_SPACING, 8)).expandedDropdownId)
    }

    @Test
    fun `toggle checked flips`() {
        val off = start.reduce(ComponentLibraryAction.ToggleChecked)
        assertFalse(off.checked)
        assertTrue(off.reduce(ComponentLibraryAction.ToggleChecked).checked)
    }

    @Test
    fun `default style maps to default renderer style`() {
        assertEquals(SageRendererStyle(), ComponentLibraryStyle().toRendererStyle())
    }

    @Test
    fun `content is deterministic per seed`() {
        assertEquals(componentLibraryContent(7L), componentLibraryContent(7L))
    }
}
