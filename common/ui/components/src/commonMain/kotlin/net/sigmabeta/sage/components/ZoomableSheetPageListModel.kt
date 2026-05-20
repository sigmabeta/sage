package net.sigmabeta.sage.components

import kotlinx.collections.immutable.ImmutableList
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.pdf.PdfConfigById

data class ZoomableSheetPageListModel(
    val pdfConfigById: PdfConfigById,
    val title: String,
    val gameName: String,
    val composers: ImmutableList<String>,
    val pageNumber: Int,
    val clickAction: SageAction,
    override val dataId: Long = ("$gameName - $title: Page $pageNumber").hashCode().toLong()
) : ListModel() {
    override val columns = COLUMNS_ALL
}
