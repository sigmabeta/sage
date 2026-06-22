package net.sigmabeta.sage.components

/**
 * An inline confirmation prompt rendered as its own lifted container (like
 * [EditTextListModel], but with read-only body text instead of a field): a header, a body line, and
 * a cancel/confirm button pair. The renderer dispatches
 * [net.sigmabeta.sage.appcomm.SageAction.ConfirmationConfirmed] (with [id]) on confirm and
 * [net.sigmabeta.sage.appcomm.SageAction.ConfirmationCancelled] (with [id]) on cancel, so the reducer
 * knows which prompt acted (e.g. which playlist to delete).
 *
 * @param id stable identifier carried back in both actions; also this model's [dataId]
 * @param header the prompt title (e.g. "Delete playlist?")
 * @param bodyText the explanatory line (e.g. "This can't be undone.")
 * @param confirmLabel label for the confirm button
 * @param cancelLabel label for the cancel button
 */
data class ConfirmationListModel(
    val id: Long,
    val header: String,
    val bodyText: String,
    val confirmLabel: String = "Ok",
    val cancelLabel: String = "Cancel",
) : ListModel() {
    override val dataId: Long = id
    override val columns = ListModel.COLUMNS_ALL
}
