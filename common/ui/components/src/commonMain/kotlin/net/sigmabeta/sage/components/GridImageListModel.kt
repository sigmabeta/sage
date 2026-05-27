package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.ui.Icon

data class GridImageListModel(
    override val dataId: Long,
    val name: String,
    val sourceInfo: String?,
    val imagePlaceholder: Icon,
    val actionableId: Long? = null,
    val clickAction: SageAction,
    val active: Boolean = false,
    // Cell aspect ratio (width / height). Defaults to 1f — square — for surfaces with no
    // native cover-art aspect (artists). Game cells pass 0.75f (3:4 IGDB cover ratio) so
    // a portrait cover renders without being cropped.
    val aspectRatio: Float = 1f,
) : ListModel() {
    override val columns = 1
}
