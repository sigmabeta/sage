package net.sigmabeta.sage.list

import kotlinx.coroutines.CoroutineScope
import net.sigmabeta.sage.coroutines.SageDispatchers

interface VglsScheduler {
    val dispatchers: SageDispatchers
    val coroutineScope: CoroutineScope
    val delayManager: DelayManager
}
