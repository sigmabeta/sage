package net.sigmabeta.sage.freeform

import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageState
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.ui.StringProvider

abstract class FreeformState<Model> : SageState {
    abstract fun title(stringProvider: StringProvider): TitleBarModel
    abstract fun toContent(stringProvider: StringProvider): Model
    abstract fun errorContent(error: Throwable): Model

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    fun toActual(stringProvider: StringProvider): FreeformStateActual<Model> {
        val title = try {
            title(stringProvider)
        } catch (ex: Exception) {
            TitleBarModel("App Error Occurred")
        }

        val content = try {
            toContent(stringProvider)
        } catch (ex: Exception) {
            errorContent(ex)
        }

        return FreeformStateActual(title = title, content = content)
    }

    protected inline fun <T, R> LCE<T>.fold(
        onUninitialized: () -> R,
        onLoading: (operationName: String) -> R,
        onError: (operationName: String, error: Throwable) -> R,
        onContent: (T) -> R,
    ): R = when (this) {
        LCE.Uninitialized -> onUninitialized()
        is LCE.Loading -> onLoading(operationName)
        is LCE.Error -> onError(operationName, error)
        is LCE.Content -> onContent(data)
    }
}
