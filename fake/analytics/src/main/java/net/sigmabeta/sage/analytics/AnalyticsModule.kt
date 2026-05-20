package net.sigmabeta.sage.analytics

import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.logging.Hatchet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.zacsweers.metro.ContributesTo
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
@ContributesTo(AppScope::class)
object AnalyticsModule {
    @Provides
    @Singleton
    fun provideAnalytics(hatchet: Hatchet): Analytics = NoopAnalytics(hatchet)
}
