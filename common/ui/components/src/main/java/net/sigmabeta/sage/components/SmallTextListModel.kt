package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.SageAction

data class SmallTextListModel(
    val name: String,
    val clickAction: SageAction,
    override val dataId: Long = name.hashCode().toLong(),
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
