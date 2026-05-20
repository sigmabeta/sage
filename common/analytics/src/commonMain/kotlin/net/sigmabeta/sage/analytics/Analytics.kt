package net.sigmabeta.sage.analytics

import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent

interface Analytics {
    fun logScreenView(
        action: SageAction,
        screen: AnalyticsScreenId,
    )

    fun logAction(
        action: SageAction,
        fromScreen: AnalyticsScreenId,
    )

    fun logEvent(
        event: SageEvent,
    )

    fun logAutoRefresh()

    fun logError(
        failedOperationName: String,
        errorString: String,
        error: Throwable,
    )
}
