package net.sigmabeta.sage.ui.componentlibrary

// No random text generator on JS; deterministic placeholders suffice for a debug gallery.
actual fun componentLibraryContent(seed: Long, count: Int): ComponentLibraryContent = ComponentLibraryContent(
    names = List(count) { "Sample Name ${it + 1}" },
    titles = List(count) { "Sample Title ${it + 1}" },
    captions = List(count) { "Sample caption ${it + 1}" },
)
