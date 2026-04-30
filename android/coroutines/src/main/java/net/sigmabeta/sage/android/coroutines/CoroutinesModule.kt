package net.sigmabeta.sage.android.coroutines

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.sigmabeta.sage.coroutines.VglsDispatchers

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesModule {
    @Singleton
    @Provides
    fun provideCoroutineScope(
        vglsDispatchers: VglsDispatchers
    ) = CoroutineScope(vglsDispatchers.computation)

    @Singleton
    @Provides
    fun provideRegularDispatchers() = VglsDispatchers(
        computation = Dispatchers.Default,
        disk = Dispatchers.IO,
        network = Dispatchers.IO,
        main = Dispatchers.Main
    )
}
