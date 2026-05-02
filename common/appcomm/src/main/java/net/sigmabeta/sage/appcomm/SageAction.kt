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

    data object KeepScreenOnSnackCtaClicked : SageAction()
}
