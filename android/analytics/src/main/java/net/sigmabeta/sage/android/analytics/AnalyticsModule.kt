package net.sigmabeta.sage.android.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.android.analytics.firebase.FirebaseAnalyticsImpl
import net.sigmabeta.sage.coroutines.SageDispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AnalyticsModule {
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context) = FirebaseAnalytics.getInstance(context)

    @Provides
    @Singleton
    fun provideAnalyticsImpl(
        firebaseAnalytics: FirebaseAnalytics,
        dispatchers: SageDispatchers,
        coroutineScope: CoroutineScope
    ): Analytics = FirebaseAnalyticsImpl(
            firebaseAnalytics,
            dispatchers,
            coroutineScope,
        )
}
