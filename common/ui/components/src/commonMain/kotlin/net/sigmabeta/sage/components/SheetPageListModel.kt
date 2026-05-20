package net.sigmabeta.sage.components

import kotlinx.collections.immutable.ImmutableList
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.pdf.PdfConfigById

data class SheetPageListModel(
    val pdfConfigById: PdfConfigById,
    val title: String,
    val gameName: String,
    val composers: ImmutableList<String>,
    val pageNumber: Int,
    val clickAction: SageAction,
    val showLyricsWarning: Boolean = false,
    override val dataId: Long = ("$gameName - $title: Page $pageNumber").hashCode().toLong()
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
