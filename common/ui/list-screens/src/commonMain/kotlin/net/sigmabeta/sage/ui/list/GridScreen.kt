package net.sigmabeta.sage.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.HorizontalScrollerListModel
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.components.SectionListModel
import net.sigmabeta.sage.list.ListStateActual

@Composable
@Suppress("MagicNumber", "LongMethod")
fun GridScreen(
    state: ListStateActual,
    actionSink: ActionSink,
    showDebug: Boolean,
    numberOfColumns: Int,
    staggered: Boolean,
    allowHorizScroller: Boolean,
    sideMargin: Dp,
    modifier: Modifier,
    itemContent: @Composable (model: ListModel, sink: ActionSink, debug: Boolean, modifier: Modifier, padding: PaddingValues) -> Unit,
) {
    val title = state.title
    val items = state.listItems

    if (title.title != null) {
        LaunchedEffect(title.title) {
            actionSink.sendAction(SageAction.Resume)
        }
    }

    val unrolledItems = items.map { topLevelItem ->
        return@map if (topLevelItem is SectionListModel) {
            if (topLevelItem.dontUnroll) {
                return@map topLevelItem
            }

            val horizontalScrollers = topLevelItem.sectionItems.filterIsInstance<HorizontalScrollerListModel>()

            if (horizontalScrollers.isNotEmpty()) {
                val otherItems = topLevelItem.sectionItems.filter { it !is HorizontalScrollerListModel }
                val flattenedItems = otherItems + horizontalScrollers.map { it.scrollingItems }.flatten()

                topLevelItem.copy(sectionItems = flattenedItems.toImmutableList())
            } else {
                topLevelItem
            }
        } else {
            topLevelItem
        }
    }

    if (staggered) {
        val (contentPadding, itemPadding, arrangement) = if (allowHorizScroller) {
            Triple(
                0.dp,
                sideMargin / 2,
                Arrangement.spacedBy(0.dp)
            )
        } else {
            Triple(
                sideMargin,
                0.dp,
                Arrangement.spacedBy(32.dp)
            )
        }

        val contentPaddingWithInsets = PaddingValues(
            top = 16.dp,
            start = contentPadding,
            end = contentPadding,
            bottom = 16.dp +
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() +
                LocalListBottomInset.current
        )

        LazyVerticalStaggeredGrid(
            contentPadding = contentPaddingWithInsets,
            columns = StaggeredGridCells.Fixed(numberOfColumns),
            horizontalArrangement = arrangement,
            modifier = modifier
                .fillMaxSize()
        ) {
            items(
                items = unrolledItems,
                key = { it.dataId },
                contentType = { it.layoutId() },
                span = {
                    if (it.columns < 1) {
                        StaggeredGridItemSpan.FullLine
                    } else {
                        StaggeredGridItemSpan.SingleLane
                    }
                }
            ) {
                itemContent(
                    it,
                    actionSink,
                    showDebug,
                    Modifier.animateItem(),
                    PaddingValues(horizontal = itemPadding),
                )
            }
        }
    } else {
        val contentPaddingWithInsets = PaddingValues(
            top = 16.dp,
            bottom = 16.dp +
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() +
                LocalListBottomInset.current,
            start = sideMargin,
            end = sideMargin,
        )

        LazyVerticalGrid(
            contentPadding = contentPaddingWithInsets,
            columns = GridCells.Fixed(numberOfColumns),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxSize()
        ) {
            items(
                items = unrolledItems,
                key = { it.dataId },
                contentType = { it.layoutId() },
                span = {
                    if (it.columns < 1) {
                        GridItemSpan(maxLineSpan)
                    } else {
                        GridItemSpan(it.columns)
                    }
                }
            ) {
                itemContent(
                    it,
                    actionSink,
                    showDebug,
                    Modifier.animateItem(),
                    PaddingValues(),
                )
            }
        }
    }
}
