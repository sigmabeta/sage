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

    data object KeepScreenOnSnackCtaClicked : SageAction()
}
