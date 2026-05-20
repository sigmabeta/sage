package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.SageAction

data class SearchHistoryListModel(
    override val dataId: Long,
    val name: String,
    val clickAction: SageAction,
    val removeAction: SageAction,
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
