package net.sigmabeta.sage.appcomm

open class SageAction {
    data object Noop : SageAction()

    data object InitNoArgs : SageAction()
    data class InitWithId(val id: Long) : SageAction()
    data class InitWithString(val arg: String) : SageAction()
    data class InitWithPageNumber(val id: Long, val pageNumber: Long) : SageAction()

    data object Resume : SageAction()
    data object Pause : SageAction()

    data object DeviceBack : SageAction()
    data object AppBack : SageAction()

    data class SnackbarActionClicked(val action: SageAction) : SageAction()
    data class SnackbarDismissed(val action: SageAction) : SageAction()

    data class SearchQueryEntered(val query: String) : SageAction()
    data object SearchClearClicked : SageAction()

    data class NotifClearClicked(val id: Long) : SageAction()

    /**
     * A drag-to-reorder gesture moved the list item at [fromIndex] to [toIndex]. Both are
     * positions within the screen's current `listItems` (header/non-reorderable rows included),
     * and the action is emitted once per completed drag — not per intermediate step. The
     * receiving reducer is the source of truth: it applies the move to its domain order and
     * re-emits; [net.sigmabeta.sage.ui.list.ReorderableScreen] only mirrors the order locally
     * for in-drag feedback.
     */
    data class Reorder(val fromIndex: Int, val toIndex: Int) : SageAction()

    /**
     * The submit (or keyboard-enter) of an inline edit-text row
     * ([net.sigmabeta.sage.components.EditTextListModel]) carrying the typed [text]. [id] is the
     * model's identifier so the reducer knows which field was confirmed (e.g. which playlist to
     * rename). Fired only when the text passes the model's `allowEmpty` rule.
     */
    data class EditTextSubmitted(val id: Long, val text: String) : SageAction()

    /**
     * The cancel of an inline edit-text row. [id] is the model's identifier so the reducer knows
     * which edit field to dismiss/remove.
     */
    data class EditTextCancelled(val id: Long) : SageAction()

    /**
     * The confirm of an inline confirmation row
     * ([net.sigmabeta.sage.components.ConfirmationListModel]). [id] is the model's identifier so the
     * reducer knows which prompt was confirmed (e.g. which playlist to delete).
     */
    data class ConfirmationConfirmed(val id: Long) : SageAction()

    /** The cancel of an inline confirmation row; [id] identifies which prompt to dismiss. */
    data class ConfirmationCancelled(val id: Long) : SageAction()

    data object KeepScreenOnSnackCtaClicked : SageAction()
}
