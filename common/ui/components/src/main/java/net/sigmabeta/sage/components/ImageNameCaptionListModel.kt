package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.VglsAction
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.ui.Icon

data class ImageNameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val sourceInfo: SourceInfo,
    val imagePlaceholder: Icon,
    val actionableId: Long? = null,
    val clickAction: VglsAction
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
