package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.ui.Icon

data class IconNameListModel(
    override val dataId: Long,
    val name: String,
    val icon: Icon,
    val clickAction: SageAction,
    val active: Boolean = false,
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
