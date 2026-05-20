package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.ui.Icon

data class CtaListModel(
    val icon: Icon,
    val name: String,
    val clickAction: SageAction,
    override val dataId: Long = name.hashCode().toLong(),
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
