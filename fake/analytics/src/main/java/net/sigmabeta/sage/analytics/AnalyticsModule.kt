package net.sigmabeta.sage.analytics

import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.logging.Hatchet

@BindingContainer
@ContributesTo(AppScope::class)
object AnalyticsModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideAnalytics(hatchet: Hatchet): Analytics = NoopAnalytics(hatchet)
}
