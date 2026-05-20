package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.ui.Icon

data class HeroImageListModel(
    val sourceInfo: SourceInfo,
    val contentDescription: String,
    val imagePlaceholder: Icon,
    val clickAction: SageAction
) : ListModel() {
    override val dataId = sourceInfo.hashCode().toLong()
    override val columns = 1
}
