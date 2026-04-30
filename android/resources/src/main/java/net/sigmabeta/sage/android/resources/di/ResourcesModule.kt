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
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ResourcesModule {
    @Provides
    @Singleton
    fun provideResources(@ApplicationContext context: Context): Resources = context.resources

    @Provides
    @Singleton
    fun provideResourceProvider(resources: Resources): ResourceProvider = RealResourceProvider(resources)
}
