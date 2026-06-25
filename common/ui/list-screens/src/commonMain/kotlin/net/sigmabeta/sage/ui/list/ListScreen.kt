package net.sigmabeta.sage.ui.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
        val paginationState = remember(listState) { listState.asPaginationState() }
        PaginationEffects(paginationState, items, actionSink)
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
