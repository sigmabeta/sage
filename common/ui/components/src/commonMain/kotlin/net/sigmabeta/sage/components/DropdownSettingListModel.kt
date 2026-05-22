package net.sigmabeta.sage.components

import kotlinx.collections.immutable.ImmutableList
import net.sigmabeta.sage.appcomm.SageAction

data class DropdownSettingListModel(
    val settingId: String,
    val name: String,
    val selectedPosition: Int,
    val settingsLabels: ImmutableList<String>,
    /**
     * Maps the picked option index to the [SageAction] to dispatch through the screen's
     * `ActionSink` (mirroring every other model's `clickAction`). Defaults to a no-op so a
     * dropdown can render a value without yet writing changes back.
     */
    val onNewOptionSelected: (Int) -> SageAction = { SageAction.Noop },
) : ListModel() {
    override val dataId: Long = settingId.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
