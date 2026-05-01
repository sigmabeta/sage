package net.sigmabeta.sage.android.coroutines

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.sigmabeta.sage.coroutines.SageDispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesModule {
    @Singleton
    @Provides
    fun provideCoroutineScope(
        sageDispatchers: SageDispatchers
    ) = CoroutineScope(sageDispatchers.computation)

    @Singleton
    @Provides
    fun provideRegularDispatchers() = SageDispatchers(
        computation = Dispatchers.Default,
        disk = Dispatchers.IO,
        network = Dispatchers.IO,
        main = Dispatchers.Main
    )
}
