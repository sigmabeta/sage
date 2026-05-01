package net.sigmabeta.sage.coroutines

import kotlinx.coroutines.CoroutineDispatcher

data class SageDispatchers(
    val computation: CoroutineDispatcher,
    val disk: CoroutineDispatcher,
    val network: CoroutineDispatcher,
    val main: CoroutineDispatcher
)
