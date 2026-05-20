package net.sigmabeta.sage.debug

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.settings.DebugSettingsManager

class RenderOverlayProvider(
    private val debugSettingsManager: DebugSettingsManager,
    private val coroutineScope: CoroutineScope,
    private val dispatchers: SageDispatchers,
) {
    private val _showRenderOverlayFlow = MutableStateFlow(false)
    val showRenderOverlayFlow = _showRenderOverlayFlow.asStateFlow()

    init {
        debugSettingsManager.getShouldShowRenderOverlay()
            .onEach { newValue -> _showRenderOverlayFlow.update { newValue } }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }
}
