package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.SageAction

data class NameCaptionValueListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val value: String,
    val clickAction: SageAction,
    val active: Boolean = false,
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
