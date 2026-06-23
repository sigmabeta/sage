package net.sigmabeta.sage.list

import net.sigmabeta.sage.components.LoadingType

/**
 * How a [ListState] pages its content, if at all. A non-[None] type opts the screen into
 * scroll-driven paging in `ListScreen`: nearing the bottom emits [net.sigmabeta.sage.appcomm.SageAction.LoadMoreRequested]
 * and nearing the top emits [net.sigmabeta.sage.appcomm.SageAction.LoadPreviousRequested]. The
 * receiving ViewModel decides what those mean; [None] screens never emit them.
 *
 * [Paginating.loadingType] is the [LoadingType] used for the inline header/footer load indicators,
 * and [Paginating.pageSize] the number of rows fetched per page.
 */
sealed class PaginationType {
    data object None : PaginationType()

    sealed class Paginating : PaginationType() {
        abstract val loadingType: LoadingType
        abstract val pageSize: Int
    }

    /**
     * Growing-window paging: each step re-queries `limit = loadedPages * pageSize` at offset 0, so
     * the visible window stays a single live query that updates when the underlying data changes.
     * Defined for future use — no engine consumes this yet.
     */
    data class Reactive(
        override val loadingType: LoadingType = LoadingType.TEXT_CAPTION,
        override val pageSize: Int = DEFAULT_PAGE_SIZE,
    ) : Paginating()

    /**
     * Offset-based paging: a contiguous window opened at an entry offset that grows in both
     * directions (append on scroll-down, prepend on scroll-up). Each page is fetched once at its
     * absolute offset and never re-queried, so memory tracks how far the user has explored rather
     * than the total catalog size.
     */
    data class Standard(
        override val loadingType: LoadingType = LoadingType.TEXT_CAPTION,
        override val pageSize: Int = DEFAULT_PAGE_SIZE,
    ) : Paginating()

    companion object {
        const val DEFAULT_PAGE_SIZE = 100
    }
}
