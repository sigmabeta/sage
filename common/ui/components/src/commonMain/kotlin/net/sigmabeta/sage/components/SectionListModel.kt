package net.sigmabeta.sage.components

import kotlinx.collections.immutable.ImmutableList

data class SectionListModel(
    override val dataId: Long,
    override val columns: Int = 1,
    val dontUnroll: Boolean = false,
    // When non-null, the (non-unrolled) section's content is capped to this width in dp and
    // centred horizontally in its slot — used to keep full-bleed content (e.g. an empty state)
    // readable on wide screens. Only honoured together with [dontUnroll].
    val maxContentWidthDp: Int? = null,
    // When true, the (non-unrolled) section's content sits on a rounded `surfaceContainer` panel,
    // matching the Now Playing setlist/info panes. Only honoured together with [dontUnroll].
    val backgroundContainer: Boolean = false,
    val sectionItems: ImmutableList<ListModel>,
) : ListModel()
