package net.sigmabeta.sage.ui.list

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState

/**
 * The slice of a lazy-scroll state that scroll-driven pagination needs, so the same effect logic
 * ([PaginationEffects]) drives both [LazyListState] (single-column [ListScreen]) and [LazyGridState]
 * (multi-column [GridScreen]). Each accessor reads snapshot-backed `layoutInfo`, so reading them
 * inside a `snapshotFlow` correctly observes scroll changes.
 */
internal interface PaginationLazyState {
    val totalItemsCount: Int
    val firstVisibleItemIndexOrNull: Int?
    val firstVisibleItemKeyOrNull: Any?
    val lastVisibleItemIndexOrNull: Int?
    val firstVisibleItemScrollOffset: Int
    suspend fun scrollToItem(index: Int, scrollOffset: Int)
}

internal fun LazyListState.asPaginationState(): PaginationLazyState = object : PaginationLazyState {
    override val totalItemsCount get() = layoutInfo.totalItemsCount
    override val firstVisibleItemIndexOrNull get() = layoutInfo.visibleItemsInfo.firstOrNull()?.index
    override val firstVisibleItemKeyOrNull get() = layoutInfo.visibleItemsInfo.firstOrNull()?.key
    override val lastVisibleItemIndexOrNull get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index
    override val firstVisibleItemScrollOffset get() = this@asPaginationState.firstVisibleItemScrollOffset
    override suspend fun scrollToItem(index: Int, scrollOffset: Int) =
        this@asPaginationState.scrollToItem(index, scrollOffset)
}

internal fun LazyGridState.asPaginationState(): PaginationLazyState = object : PaginationLazyState {
    override val totalItemsCount get() = layoutInfo.totalItemsCount
    override val firstVisibleItemIndexOrNull get() = layoutInfo.visibleItemsInfo.firstOrNull()?.index
    override val firstVisibleItemKeyOrNull get() = layoutInfo.visibleItemsInfo.firstOrNull()?.key
    override val lastVisibleItemIndexOrNull get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index
    override val firstVisibleItemScrollOffset get() = this@asPaginationState.firstVisibleItemScrollOffset
    override suspend fun scrollToItem(index: Int, scrollOffset: Int) =
        this@asPaginationState.scrollToItem(index, scrollOffset)
}
