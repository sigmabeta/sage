package net.sigmabeta.sage.android.resources.di

import android.content.Context
import android.content.res.Resources
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import net.sigmabeta.sage.android.resources.RealResourceProvider
import net.sigmabeta.sage.android.resources.ResourceProvider
import net.sigmabeta.sage.di.AppScope

@BindingContainer
@ContributesTo(AppScope::class)
object ResourcesModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideResources(context: Context): Resources = context.resources

    @Provides
    @SingleIn(AppScope::class)
    fun provideResourceProvider(resources: Resources): ResourceProvider = RealResourceProvider(resources)
}
