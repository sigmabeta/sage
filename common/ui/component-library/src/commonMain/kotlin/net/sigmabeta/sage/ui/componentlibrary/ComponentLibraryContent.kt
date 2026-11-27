package net.sigmabeta.sage.ui.componentlibrary

/**
 * Pre-generated deterministic text used to populate the gallery. Held in state so the strings stay
 * stable across recompositions (regenerating per catalog call would reshuffle every label).
 */
data class ComponentLibraryContent(
    /** Person-like names. */
    val names: List<String>,
    /** Short titles. */
    val titles: List<String>,
    /** Short secondary lines. */
    val captions: List<String>,
) {
    /** The name at [index], wrapping around. */
    fun name(index: Int) = names[index % names.size]

    /** The title at [index], wrapping around. */
    fun title(index: Int) = titles[index % titles.size]

    /** The caption at [index], wrapping around. */
    fun caption(index: Int) = captions[index % captions.size]
}

/**
 * Builds [count] of each kind of sample string; a given [seed] always yields the same content.
 * JVM/Android use SAGE's `StringGenerator`; JS returns deterministic placeholders.
 */
expect fun componentLibraryContent(seed: Long = 0L, count: Int = 12): ComponentLibraryContent
