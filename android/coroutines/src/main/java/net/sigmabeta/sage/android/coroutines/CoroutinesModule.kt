package net.sigmabeta.sage.android.coroutines

import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.di.AppScope

@BindingContainer
@ContributesTo(AppScope::class)
object CoroutinesModule {
    @SingleIn(AppScope::class)
    @Provides
    fun provideCoroutineScope(
        sageDispatchers: SageDispatchers
    ): CoroutineScope = CoroutineScope(sageDispatchers.computation)

    @SingleIn(AppScope::class)
    @Provides
    fun provideRegularDispatchers(): SageDispatchers = SageDispatchers(
        computation = Dispatchers.Default,
        disk = Dispatchers.IO,
        network = Dispatchers.IO,
        main = Dispatchers.Main
    )
}
