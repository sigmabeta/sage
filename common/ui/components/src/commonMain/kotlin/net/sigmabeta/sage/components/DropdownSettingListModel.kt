package net.sigmabeta.sage.components

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import net.sigmabeta.sage.appcomm.SageAction

data class DropdownSettingListModel(
    val settingId: String,
    val name: String,
    val selectedPosition: Int,
    /**
     * The selectable options. Each pairs a short display label with the [ListModel] rendered for
     * it in the expanded list. The label is what the collapsed row shows inline next to the caret
     * for the selected option, so the collapsed state never has to render a full component; the
     * paired [ListModel] is the expanded row, and can be plain text, a captioned row, an icon row,
     * an image row, and so on. The picked option dispatches its own `clickAction`; [ofLabels] wires
     * those from [onNewOptionSelected] for the common plain-text case.
     */
    val options: ImmutableList<Pair<String, ListModel>>,
    /**
     * Whether this dropdown is currently expanded. Hoisted into the screen's state (which tracks at
     * most one expanded dropdown), not held by the renderer — so the renderer is stateless and
     * opening one dropdown can close another.
     */
    val expanded: Boolean = false,
    /**
     * Dispatched when the collapsed/expanded header row is clicked. The screen toggles its tracked
     * expanded dropdown in response. Defaults to a no-op so a dropdown can render without being
     * interactive.
     */
    val onExpandClicked: SageAction = SageAction.Noop,
) : ListModel() {
    override val dataId: Long = settingId.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL

    companion object {
        /**
         * Builds a dropdown whose options are single-line text rows ([SingleTextListModel]), using
         * each label both as the collapsed display string and the expanded row's text, wiring each
         * row's click to [onNewOptionSelected] and marking the row at [selectedPosition] active.
         * Use the primary constructor with explicit [options] when the rows need captions or richer
         * content.
         */
        fun ofLabels(
            settingId: String,
            name: String,
            selectedPosition: Int,
            labels: ImmutableList<String>,
            expanded: Boolean = false,
            onExpandClicked: SageAction = SageAction.Noop,
            onNewOptionSelected: (Int) -> SageAction = { SageAction.Noop },
        ): DropdownSettingListModel = DropdownSettingListModel(
            settingId = settingId,
            name = name,
            selectedPosition = selectedPosition,
            options = labels.mapIndexed { index, label ->
                label to SingleTextListModel(
                    name = label,
                    clickAction = onNewOptionSelected(index),
                    active = index == selectedPosition,
                    dataId = index.toLong(),
                )
            }.toImmutableList(),
            expanded = expanded,
            onExpandClicked = onExpandClicked,
        )
    }
}
