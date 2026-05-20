package net.sigmabeta.sage.list

import kotlinx.coroutines.CoroutineScope
import net.sigmabeta.sage.nav.RouteDescriptor

interface BrainProvider {
    fun provideBrain(destination: RouteDescriptor, coroutineScope: CoroutineScope): ListViewModelBrain
}
