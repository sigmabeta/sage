package net.sigmabeta.sage.android.analytics.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.analytics.AnalyticsScreenId
import net.sigmabeta.sage.analytics.getDetails
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.coroutines.SageDispatchers

open class FirebaseAnalyticsImpl(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val dispatchers: SageDispatchers,
    private val coroutineScope: CoroutineScope,
) : Analytics {
    override fun logScreenView(
        action: SageAction,
        screen: AnalyticsScreenId
    ) {
        val detailsBundle = Bundle()

        detailsBundle.putString(PARAM_SCREEN, screen.toString())
        detailsBundle.putString(PARAM_SCREEN_DETAILS, action.getDetails())

        logEventInBackground(EVENT_SCREEN_VIEW, detailsBundle)
    }

    override fun logAction(action: SageAction, fromScreen: AnalyticsScreenId) {
        val detailsBundle = Bundle()

        detailsBundle.putString(PARAM_ACTION_NAME, action.javaClass.simpleName)
        detailsBundle.putString(PARAM_FROM_SCREEN, fromScreen.toString())

        logEventInBackground(EVENT_ACTION, detailsBundle)
    }

    override fun logEvent(event: SageEvent) {
        val detailsBundle = Bundle()

        detailsBundle.putString(PARAM_EVENT_NAME, event.javaClass.simpleName)

        logEventInBackground(EVENT_SAGE_EVENT, detailsBundle)
    }

    override fun logAutoRefresh() = logEventInBackground(EVENT_AUTO_REFRESH)

    override fun logError(failedOperationName: String, errorString: String, error: Throwable) {
        val details = Bundle()

        details.putString(PARAM_ERROR_MESSAGE, errorString)
        details.putString(PARAM_ERROR_OP_NAME, failedOperationName)
        details.putString(PARAM_ERROR_THROWABLE, error.stackTraceToString().take(ERR_STRING_LENGTH))

        logEventInBackground(EVENT_APP_ERROR, details)
    }

    protected fun logEventInBackground(eventName: String, detailsBundle: Bundle) {
        coroutineScope.launch(dispatchers.network) {
            firebaseAnalytics.logEvent(eventName, detailsBundle)
        }
    }

    protected fun logEventInBackground(name: String) {
        coroutineScope.launch(dispatchers.network) {
            firebaseAnalytics.logEvent(name, null)
        }
    }

    companion object {
        const val EVENT_ACTION = "sage_action"
        const val EVENT_SAGE_EVENT = "sage_event"
        const val EVENT_AUTO_REFRESH = "auto_refresh"
        const val EVENT_SCREEN_VIEW = "screen_view_custom"
        const val EVENT_APP_ERROR = "error_app"

        const val PARAM_SCREEN = "screen_name"
        const val PARAM_SCREEN_DETAILS = "screen_details"
        const val PARAM_ACTION_NAME = "action_name"
        const val PARAM_EVENT_NAME = "event_name"
        const val PARAM_FROM_SCREEN = "from_screen"
        const val PARAM_ERROR_OP_NAME = "error_op_name"
        const val PARAM_ERROR_MESSAGE = "error_message"
        const val PARAM_ERROR_THROWABLE = "error_throwable"

        const val ERR_STRING_LENGTH = 128
    }
}
