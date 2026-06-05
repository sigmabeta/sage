package net.sigmabeta.sage.analytics

import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.logging.Hatchet

open class NoopAnalytics(
    private val hatchet: Hatchet
) : Analytics {
    override fun logAutoRefresh() {
        hatchet.d("Refresh performed automatically.")
    }

    override fun logError(failedOperationName: String, errorString: String, error: Throwable) {
        hatchet.e("Logging error to analytics: $failedOperationName | $errorString | $error")
    }

    override fun logScreenView(
        action: SageAction,
        screen: AnalyticsScreenId
    ) {
        hatchet.v("Screen view: $screen:${action.getDetails()}")
    }

    override fun logAction(action: SageAction, fromScreen: AnalyticsScreenId) {
        hatchet.d("Logging Action $action")
    }

    override fun logEvent(event: SageEvent) {
        hatchet.d("Logging Event $event")
    }
}
