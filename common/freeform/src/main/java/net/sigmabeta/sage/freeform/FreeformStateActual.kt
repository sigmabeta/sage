package net.sigmabeta.sage.freeform

import net.sigmabeta.sage.components.TitleBarModel

data class FreeformStateActual<Model>(
    val title: TitleBarModel,
    val content: Model,
)
