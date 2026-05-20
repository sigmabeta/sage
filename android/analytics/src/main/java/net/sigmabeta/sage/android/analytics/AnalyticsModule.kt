package net.sigmabeta.sage.android.analytics

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
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)
}
