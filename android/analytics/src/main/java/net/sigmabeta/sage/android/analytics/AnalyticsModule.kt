package net.sigmabeta.sage.android.analytics

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.zacsweers.metro.ContributesTo
import javax.inject.Singleton
import net.sigmabeta.sage.di.AppScope

@InstallIn(SingletonComponent::class)
@Module
@ContributesTo(AppScope::class)
object AnalyticsModule {
    // firebase-analytics's manifest declares INTERNET / ACCESS_NETWORK_STATE / WAKE_LOCK
    // transitively, and lint sees those at app merge time — but the module-level lint check
    // can't, so it flags MissingPermission. Surfaced when the catalog renamed firebase-
    // analytics-ktx → firebase-analytics; the older artifact's lint metadata didn't run this
    // check on the call site.
    @Provides
    @Singleton
    @SuppressLint("MissingPermission")
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context)
}
