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
    // Optional cap on the cell's rendered width, in dp. Null lets the cell fill the space it's
    // given (the usual carousel/grid behaviour). A value caps the width and centers the cell —
    // used for a single full-width item (e.g. the "game of the day" hero) so it doesn't span the
    // whole screen. Kept as a plain Float so this model stays Compose-free.
    val maxWidthDp: Float? = null,
) : ListModel() {
    override val columns = 1
}
