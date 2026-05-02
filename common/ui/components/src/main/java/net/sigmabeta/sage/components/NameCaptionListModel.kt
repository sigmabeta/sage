package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.SageAction

data class NameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val clickAction: SageAction,
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
