package net.sigmabeta.sage.components

internal actual fun runtimeClassName(obj: Any): String = obj::class.simpleName ?: ""
