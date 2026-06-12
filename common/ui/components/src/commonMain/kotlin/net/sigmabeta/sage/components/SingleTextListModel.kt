package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.SageAction

data class SingleTextListModel(
    val name: String,
    val clickAction: SageAction,
    val active: Boolean = false,
    override val dataId: Long = name.hashCode().toLong(),
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
