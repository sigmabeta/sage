package net.sigmabeta.sage.images

/**
 * An image drawn as a collage of other images — e.g. a person with no photo of their own, shown
 * through the covers of the works they're credited on. Carried as a [SourceInfo.info]; the
 * consuming renderer lays [sources] out in tiles (see `:common:ui:composables`) and loads each one as
 * it would a single image.
 */
data class ImageCollage(
    val sources: List<String>,
) {
    init {
        require(sources.size in 2..MAX_SOURCES) {
            "A collage needs 2 to $MAX_SOURCES sources, got ${sources.size}."
        }
    }

    companion object {
        /** The most tiles a collage shows: a 2×2 grid. */
        const val MAX_SOURCES = 4
    }
}
