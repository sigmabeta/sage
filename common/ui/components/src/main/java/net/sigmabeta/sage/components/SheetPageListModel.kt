package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.VglsAction
import net.sigmabeta.sage.pdf.PdfConfigById
import kotlinx.collections.immutable.ImmutableList

data class SheetPageListModel(
    val pdfConfigById: PdfConfigById,
    val title: String,
    val gameName: String,
    val composers: ImmutableList<String>,
    val pageNumber: Int,
    val clickAction: VglsAction,
    val showLyricsWarning: Boolean = false,
    override val dataId: Long = ("$gameName - $title: Page $pageNumber").hashCode().toLong()
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
