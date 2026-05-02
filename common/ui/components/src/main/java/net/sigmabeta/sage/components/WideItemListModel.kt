package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.ui.Icon

data class WideItemListModel(
    override val dataId: Long,
    val name: String,
    val sourceInfo: String?,
    val imagePlaceholder: Icon,
    val actionableId: Long? = null,
    val clickAction: SageAction
) : ListModel() {
    override val columns = 1
}
