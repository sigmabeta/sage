package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.ui.Icon

data class MenuItemListModel(
    val name: String,
    val caption: String?,
    val icon: Icon,
    val clickAction: SageAction,
    val selected: Boolean = false
) : ListModel() {
    override val dataId = name.hashCode().toLong()
    override val columns = COLUMNS_ALL
}
