package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.SageAction

data class LabelRatingStarListModel(
    val label: String,
    val value: Int,
    val clickAction: SageAction,
    override val dataId: Long = label.hashCode().toLong()
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
