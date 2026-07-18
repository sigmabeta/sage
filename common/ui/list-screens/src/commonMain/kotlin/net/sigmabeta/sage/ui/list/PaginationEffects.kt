package net.sigmabeta.sage.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.ListModel

/**
 * Scroll-driven paging signals for a lazy list/grid whose state opts into pagination. Nearing the
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
internal fun PaginationEffects(
    scrollState: PaginationLazyState,
    items: List<ListModel>,
    actionSink: ActionSink,
) {
    var anchorKey by remember { mutableStateOf<Any?>(null) }
    var anchorOffset by remember { mutableStateOf(0) }
    var anchorIndex by remember { mutableStateOf(-1) }

    LaunchedEffect(scrollState) {
        snapshotFlow {
            val total = scrollState.totalItemsCount
            val last = scrollState.lastVisibleItemIndexOrNull ?: -1
            total > 0 && last >= total - LOAD_MORE_THRESHOLD
        }
            .distinctUntilChanged()
            .filter { it }
            .collect { actionSink.sendAction(SageAction.LoadMoreRequested) }
    }

    LaunchedEffect(scrollState) {
        snapshotFlow {
            val first = scrollState.firstVisibleItemIndexOrNull ?: Int.MAX_VALUE
            scrollState.totalItemsCount > 0 && first <= LOAD_MORE_THRESHOLD
        }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                anchorKey = scrollState.firstVisibleItemKeyOrNull
                anchorOffset = scrollState.firstVisibleItemScrollOffset
                anchorIndex = scrollState.firstVisibleItemIndexOrNull ?: 0
                actionSink.sendAction(SageAction.LoadPreviousRequested)
            }
    }

    LaunchedEffect(items) {
        val key = anchorKey ?: return@LaunchedEffect
        val newIndex = items.indexOfFirst { it.dataId == key }
        when {
            newIndex < 0 -> anchorKey = null

            // anchor row left the window
            newIndex > anchorIndex -> { // rows prepended above the anchor
                scrollState.scrollToItem(newIndex, anchorOffset)
                anchorIndex = newIndex
            }

            else -> anchorKey = null // index stable -> prepend settled
        }
    }
}

private const val LOAD_MORE_THRESHOLD = 5
