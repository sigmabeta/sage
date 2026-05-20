package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.SageAction

data class NotifListModel(
    override val dataId: Long,
    val title: String,
    val description: String,
    val actionLabel: String,
    val action: SageAction?,
    val isError: Boolean
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
