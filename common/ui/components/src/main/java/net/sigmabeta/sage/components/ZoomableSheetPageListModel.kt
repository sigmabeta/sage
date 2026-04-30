package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.VglsAction
import net.sigmabeta.sage.pdf.PdfConfigById
import kotlinx.collections.immutable.ImmutableList

data class ZoomableSheetPageListModel(
    val pdfConfigById: PdfConfigById,
    val title: String,
    val gameName: String,
    val composers: ImmutableList<String>,
    val pageNumber: Int,
    val clickAction: VglsAction,
    override val dataId: Long = ("$gameName - $title: Page $pageNumber").hashCode().toLong()
) : ListModel() {
    override val columns = COLUMNS_ALL
}
