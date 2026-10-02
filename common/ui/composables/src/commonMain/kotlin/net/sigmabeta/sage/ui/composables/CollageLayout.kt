package net.sigmabeta.sage.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.sigmabeta.sage.images.ImageCollage

/**
 * Lays out up to [ImageCollage.MAX_SOURCES] tiles to fill [modifier]'s bounds, adapting to how many
 * there are:
 *
 * - 1: one full tile
 * - 2: two side-by-side halves
 * - 3: a tall left half, plus two stacked quarters on the right
 * - 4: a 2×2 grid
 *
 * Tiles past the fourth are dropped. [tile] draws tile `index` and must apply the `Modifier` it's
 * handed, which fills its slot. Each slot is a [Box] sized by the layout, so the tile is free to
 * wrap its content in its own containers (e.g. a `Crossfade`) without breaking the arrangement.
 */
@Composable
fun CollageLayout(
    tileCount: Int,
    modifier: Modifier = Modifier,
    spacing: Dp = 0.dp,
    tile: @Composable (index: Int, modifier: Modifier) -> Unit,
) {
    val arrangement = Arrangement.spacedBy(spacing)

    when (tileCount.coerceAtMost(ImageCollage.MAX_SOURCES)) {
        0 -> Unit

        1 -> Slot(0, modifier, tile)

        2 -> Row(modifier, horizontalArrangement = arrangement) {
            Slot(0, Modifier.weight(1f), tile)
            Slot(1, Modifier.weight(1f), tile)
        }

        3 -> Row(modifier, horizontalArrangement = arrangement) {
            Slot(0, Modifier.weight(1f), tile)
            Column(Modifier.weight(1f), verticalArrangement = arrangement) {
                Slot(1, Modifier.weight(1f), tile)
                Slot(2, Modifier.weight(1f), tile)
            }
        }

        else -> Column(modifier, verticalArrangement = arrangement) {
            Row(Modifier.weight(1f), horizontalArrangement = arrangement) {
                Slot(0, Modifier.weight(1f), tile)
                Slot(1, Modifier.weight(1f), tile)
            }
            Row(Modifier.weight(1f), horizontalArrangement = arrangement) {
                Slot(2, Modifier.weight(1f), tile)
                Slot(3, Modifier.weight(1f), tile)
            }
        }
    }
}

@Composable
private fun Slot(
    index: Int,
    modifier: Modifier,
    tile: @Composable (index: Int, modifier: Modifier) -> Unit,
) {
    Box(modifier.fillMaxSize()) {
        tile(index, Modifier.fillMaxSize())
    }
}
