package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.SageAction

data class CheckableListModel(
    val settingId: String,
    val name: String,
    val checked: Boolean?,
    val clickAction: SageAction,
) : ListModel() {
    override val dataId: Long = settingId.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
