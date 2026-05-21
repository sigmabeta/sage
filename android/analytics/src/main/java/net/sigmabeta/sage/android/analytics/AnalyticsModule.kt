package net.sigmabeta.sage.android.analytics

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import net.sigmabeta.sage.di.AppScope

@BindingContainer
@ContributesTo(AppScope::class)
object AnalyticsModule {
    // firebase-analytics's manifest declares INTERNET / ACCESS_NETWORK_STATE / WAKE_LOCK
    // transitively, and lint sees those at app merge time — but the module-level lint check
    // can't, so it flags MissingPermission. Surfaced when the catalog renamed firebase-
    // analytics-ktx → firebase-analytics; the older artifact's lint metadata didn't run this
    // check on the call site.
    @Provides
    @SingleIn(AppScope::class)
    @SuppressLint("MissingPermission")
    fun provideFirebaseAnalytics(context: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context)
}
