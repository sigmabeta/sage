package net.sigmabeta.sage.ui.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.list.ListStateActual
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

/**
 * Single-column sibling of [ListScreen] / [GridScreen] whose rows can be reordered by drag.
 *
 * Reorder is relational, so it can't live in an individual row: this screen owns the
 * [androidx.compose.foundation.lazy.LazyListState] and the drag orchestration (via
 * sh.calvin.reorderable), and hands each row a ready-made [Modifier] to attach to its drag
 * handle plus an `isDragging` flag for lift styling. The row composables themselves stay
 * library-agnostic — `itemContent` exposes only `Modifier`/`Boolean`, never a reorderable type,
 * so the dependency remains an `implementation` detail of this module.
 *
 * **Order ownership.** The caller's reducer is the source of truth: a completed drag emits a
 * single [SageAction.Reorder] (net from→to), and the reducer re-emits `state.listItems` in the
 * new order. To keep the drag itself smooth without round-tripping every frame, this screen
 * mirrors `listItems` into a local snapshot list it mutates live; that mirror is rebuilt from
 * `state.listItems` whenever the reducer emits, so the reducer always wins.
 */
@Composable
fun ReorderableScreen(
    state: ListStateActual,
    actionSink: ActionSink,
    showDebug: Boolean,
    sideMargin: Dp,
    modifier: Modifier,
    itemContent: @Composable (
        model: ListModel,
        sink: ActionSink,
        debug: Boolean,
        isDragging: Boolean,
        dragHandle: Modifier,
        modifier: Modifier,
        padding: PaddingValues,
    ) -> Unit,
) {
    val title = state.title

    if (title.title != null) {
        LaunchedEffect(title.title) {
            actionSink.sendAction(SageAction.Resume)
        }
    }

    // Local mirror of the canonical order, mutated live during a drag for smooth visuals.
    // Keyed on state.listItems: when the reducer re-emits (post-Reorder, or any other change)
    // this rebuilds from its order, so the reducer remains the source of truth.
    val items = remember(state.listItems) { state.listItems.toMutableStateList() }

    // (dataId, indexAtDragStart) for the row currently being dragged; null when idle. Used to
    // emit one net Reorder on drop instead of one per intermediate swap.
    val dragStart = remember { mutableStateOf<Pair<Long, Int>?>(null) }

    val listState = rememberLazyListState()
    val reorderState = rememberReorderableLazyListState(listState) { from, to ->
        items.add(to.index, items.removeAt(from.index))
    }
    val haptic = LocalHapticFeedback.current

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
        itemsIndexed(
            items = items,
            key = { _, item -> item.dataId },
            contentType = { _, item -> item.layoutId() }
        ) { index, item ->
            ReorderableItem(reorderState, key = item.dataId) { isDragging ->
                val dragHandle = Modifier.draggableHandle(
                    onDragStarted = {
                        dragStart.value = item.dataId to index
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                    onDragStopped = {
                        val start = dragStart.value
                        dragStart.value = null
                        if (start != null) {
                            val finalIndex = items.indexOfFirst { it.dataId == start.first }
                            if (finalIndex >= 0 && finalIndex != start.second) {
                                actionSink.sendAction(SageAction.Reorder(start.second, finalIndex))
                            }
                        }
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                )

                itemContent(
                    item,
                    actionSink,
                    showDebug,
                    isDragging,
                    dragHandle,
                    Modifier,
                    PaddingValues(horizontal = sideMargin),
                )
            }
        }
    }
}
