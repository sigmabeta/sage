package net.sigmabeta.sage.android.resources.di

import android.content.Context
import android.content.res.Resources
import net.sigmabeta.sage.android.resources.RealResourceProvider
import net.sigmabeta.sage.android.resources.ResourceProvider
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
object ResourcesModule {
    @Provides
    @Singleton
    fun provideResources(@ApplicationContext context: Context): Resources = context.resources

    @Provides
    @Singleton
    fun provideResourceProvider(resources: Resources): ResourceProvider = RealResourceProvider(resources)
}
