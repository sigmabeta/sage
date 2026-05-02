package net.sigmabeta.sage.appcomm

open class SageEvent {
    open val source: String = "Unknown"

    data class NavigateBack(override val source: String) : SageEvent()
    data class NavigateTo(val destination: String, override val source: String) : SageEvent()
    data class NavigateSingleTopLevel(val destination: String, override val source: String) : SageEvent()
    data class NavigateSuccessTo(val destination: String) : SageEvent()

    data class ShowSnackbar(
        val message: String,
        val withDismissAction: Boolean,
        val actionDetails: SnackbarActionDetails? = null,
        override val source: String,
    ) : SageEvent() {
        data class SnackbarActionDetails(
            val actionSink: ActionSink,
            val clickAction: SageAction,
            val clickActionLabel: String,
        )
    }

    data class UpdateTitle(
        val title: String? = null,
        val subtitle: String? = null,
        val shouldShowBack: Boolean = true,
        override val source: String,
    ) : SageEvent()

    data object HideTopBar : SageEvent()

    data object ShowUiChrome : SageEvent()
    data object HideUiChrome : SageEvent()

    data object SystemBarsBecameShown : SageEvent()
    data object SystemBarsBecameHidden : SageEvent()

    data class ClearNotif(val id: Long) : SageEvent()

    data object ScreenOnTimerStarted : SageEvent()
    data class ScreenOnTimerEnded(val reason: String) : SageEvent()
}
