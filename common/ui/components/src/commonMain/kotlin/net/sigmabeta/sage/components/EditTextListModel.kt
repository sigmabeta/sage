package net.sigmabeta.sage.components

/**
 * An inline, editable text-entry row rendered as its own lifted container (like a dropdown): a text
 * field on top and a cancel/submit button pair below. The renderer holds the typed text locally and
 * dispatches [net.sigmabeta.sage.appcomm.SageAction.EditTextSubmitted] (with the text + [id]) on
 * submit or keyboard-enter, and [net.sigmabeta.sage.appcomm.SageAction.EditTextCancelled] (with
 * [id]) on cancel. The [id] lets the reducer tell which field acted — e.g. which playlist to rename,
 * or which transient edit row to remove.
 *
 * @param id stable identifier carried back in both actions; also this model's [dataId]
 * @param hint placeholder shown while the field is empty
 * @param header optional title shown above the field (e.g. "Rename playlist"); hidden when blank
 * @param initialText the field's starting value (e.g. the current name being edited); empty by default
 * @param submitLabel label for the confirm button
 * @param cancelLabel label for the cancel button
 * @param allowEmpty when false, submit (button + keyboard-enter) is blocked while the text is blank
 * @param autoFocus when true, the text field requests focus on first render. Only set this when at
 *   most one edit-text row is on screen — multiple auto-focusing rows would fight over focus.
 */
data class EditTextListModel(
    val id: Long,
    val hint: String,
    val header: String = "",
    val initialText: String = "",
    val submitLabel: String = "Ok",
    val cancelLabel: String = "Cancel",
    val allowEmpty: Boolean = false,
    // Only set true when a single edit-text row is on screen (e.g. an inline rename); with several,
    // each would request focus on render and fight over it.
    val autoFocus: Boolean = false,
) : ListModel() {
    override val dataId: Long = id
    override val columns = ListModel.COLUMNS_ALL
}
