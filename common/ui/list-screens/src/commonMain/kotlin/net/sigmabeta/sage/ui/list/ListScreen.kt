package net.sigmabeta.sage.ui.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.list.ListStateActual
import net.sigmabeta.sage.list.PaginationType

@Composable
fun ListScreen(
    state: ListStateActual,
    actionSink: ActionSink,
    showDebug: Boolean,
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

    val listState = rememberLazyListState()

    if (state.paginationType != PaginationType.None) {
        PaginationEffects(listState, items, actionSink)
    }

    val contentPadding = PaddingValues(
        top = 16.dp,
        bottom = 16.dp +
            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() +
            LocalListBottomInset.current
    )

    LazyColumn(
        state = listState,
        contentPadding = contentPadding,
        modifier = modifier
            .fillMaxSize()
    ) {
        items(
            items = items,
            key = { it.dataId },
            contentType = { it.layoutId() }
        ) {
            itemContent(
                it,
                actionSink,
                showDebug,
                Modifier.animateItem(),
                PaddingValues(horizontal = sideMargin),
            )
        }
    }
}

/**
 * Scroll-driven paging signals for a [ListScreen] whose state opts into pagination. Nearing the
 * bottom emits [SageAction.LoadMoreRequested]; nearing the top emits [SageAction.LoadPreviousRequested].
 *
 * Appending below the viewport doesn't move it, so load-more needs no compensation. Prepending
 * above the viewport would shift everything down, so before requesting a previous page we capture
 * the first visible row as an anchor (its key, pixel offset, and index) and, as the prepend lands,
 * re-scroll to keep that row pinned. A prepend arrives as two emissions — first the header loader
 * row, then the fetched rows — so we compensate on every emission whose anchor index grew, and only
 * disarm once the index stops growing (load settled) or the anchor leaves the list.
 */
@Composable
private fun PaginationEffects(
    listState: LazyListState,
    items: List<ListModel>,
    actionSink: ActionSink,
) {
    var anchorKey by remember { mutableStateOf<Any?>(null) }
    var anchorOffset by remember { mutableStateOf(0) }
    var anchorIndex by remember { mutableStateOf(-1) }

    LaunchedEffect(listState) {
        snapshotFlow {
            val info = listState.layoutInfo
            val total = info.totalItemsCount
            val last = info.visibleItemsInfo.lastOrNull()?.index ?: -1
            total > 0 && last >= total - LOAD_MORE_THRESHOLD
        }
            .distinctUntilChanged()
            .filter { it }
            .collect { actionSink.sendAction(SageAction.LoadMoreRequested) }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            val info = listState.layoutInfo
            val first = info.visibleItemsInfo.firstOrNull()?.index ?: Int.MAX_VALUE
            info.totalItemsCount > 0 && first <= LOAD_MORE_THRESHOLD
        }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                val first = listState.layoutInfo.visibleItemsInfo.firstOrNull()
                anchorKey = first?.key
                anchorOffset = listState.firstVisibleItemScrollOffset
                anchorIndex = first?.index ?: 0
                actionSink.sendAction(SageAction.LoadPreviousRequested)
            }
    }

    LaunchedEffect(items) {
        val key = anchorKey ?: return@LaunchedEffect
        val newIndex = items.indexOfFirst { it.dataId == key }
        when {
            newIndex < 0 -> anchorKey = null                       // anchor row left the window
            newIndex > anchorIndex -> {                            // rows prepended above the anchor
                listState.scrollToItem(newIndex, anchorOffset)
                anchorIndex = newIndex
            }
            else -> anchorKey = null                              // index stable -> prepend settled
        }
    }
}

private const val LOAD_MORE_THRESHOLD = 5
