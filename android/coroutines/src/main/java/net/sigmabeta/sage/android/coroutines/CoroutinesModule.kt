package net.sigmabeta.sage.android.coroutines

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.zacsweers.metro.ContributesTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.di.AppScope
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
@ContributesTo(AppScope::class)
object CoroutinesModule {
    @Singleton
    @Provides
    fun provideCoroutineScope(
        sageDispatchers: SageDispatchers
    ): CoroutineScope = CoroutineScope(sageDispatchers.computation)

    @Singleton
    @Provides
    fun provideRegularDispatchers(): SageDispatchers = SageDispatchers(
        computation = Dispatchers.Default,
        disk = Dispatchers.IO,
        network = Dispatchers.IO,
        main = Dispatchers.Main
    )
}
