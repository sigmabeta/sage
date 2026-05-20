package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.ui.Icon

data class SearchResultListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val sourceInfo: SourceInfo,
    val imagePlaceholder: Icon,
    val actionableId: Long? = null,
    val clickAction: SageAction
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
