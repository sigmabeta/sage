package net.sigmabeta.sage.images

/**
 * How the container a destination's artwork travelled from rounds its corners.
 *
 * The hero animates its own clip from this to its resting shape while the artwork flies, so the
 * corners change over the same window the bounds do instead of switching in the first frame.
 */
sealed interface ArtworkCorner {
    /** The wire form used in a route's artwork parameter. */
    val value: String

    /** A plain dp radius, e.g. a grid cell's 16 dp. */
    data class Rounded(val radiusDp: Int) : ArtworkCorner {
        override val value: String get() = radiusDp.toString()
    }

    /** A circle: a list row's thumbnail. */
    data object Circle : ArtworkCorner {
        override val value: String get() = "circle"
    }

    companion object {
        /** Parses [value]'s wire form, or `null` when it names neither a radius nor a circle. */
        fun parse(value: String): ArtworkCorner? = when (value) {
            Circle.value -> Circle

            else -> value.toIntOrNull()
                ?.takeIf { it >= 0 }
                ?.let(::Rounded)
        }
    }
}
