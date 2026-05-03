package net.sigmabeta.sage.analytics

import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent

@Suppress("TooManyFunctions")
interface Analytics {
    fun logScreenView(
        action: SageAction,
        screen: AnalyticsScreen,
    )

    /**
     * Screens that matter
     */

    fun logGameView(
        gameName: String,
    )

    fun logComposerView(
        composerName: String,
    )

    @Suppress("LongParameterList")
    fun logSongView(
        id: Long,
        songName: String,
        gameName: String,
        transposition: String?,
    )

    fun logSageAction(
        action: SageAction,
        fromScreen: AnalyticsScreen,
    )

    fun logSageEvent(
        event: SageEvent,
    )

    /**
     * Misc events
     */

    fun logAutoRefresh()

    fun logRandomSongView(songName: String, gameName: String, transposition: String)

    fun logError(
        failedOperationName: String,
        errorString: String,
        error: Throwable,
    )
}
