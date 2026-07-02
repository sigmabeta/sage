# `:common:ui:list-screens`

> The Compose Multiplatform renderers for `:common:list` — `ListScreen`, `GridScreen`, `ReorderableScreen`.

The three pure Compose Multiplatform composables that turn a `ListStateActual`
(from `:common:list`) into a scrollable screen, plus the scroll-driven
pagination machinery and bottom-inset hook they share. It is an unsuffixed
shared UI module (composables, no `:api`/`:real` split); it depends on
`:common:list` for the state types it renders and on `:common:ui:components`
for the `ListModel`s it lays out.

## Contents

- **Screen composables** — `ListScreen.kt` (single-column `LazyColumn`),
  `GridScreen.kt` (`LazyVerticalGrid` or, when `staggered`,
  `LazyVerticalStaggeredGrid`; unrolls `SectionListModel`/`HorizontalScrollerListModel`
  into the grid and computes per-item spans from `ListModel.columns`), and
  `ReorderableScreen.kt` (single-column `LazyColumn` with drag-to-reorder via
  `sh.calvin.reorderable`). Each takes a `ListStateActual`, an `ActionSink`, and
  an `itemContent` slot, fires `SageAction.Resume` on title change, and applies a
  bottom content-padding built from the navigation-bar inset plus
  `LocalListBottomInset`.
- **Pagination** — `PaginationLazyState.kt` (an internal interface abstracting the
  slice of `LazyListState`/`LazyGridState` paging needs, with `asPaginationState()`
  adapters for each) and `PaginationEffects.kt` (the `snapshotFlow`-driven effects
  that emit `SageAction.LoadMoreRequested`/`LoadPreviousRequested` near the edges
  and re-pin the viewport when rows are prepended). Engaged only when
  `state.paginationType != PaginationType.None`.
- **Insets** — `ListInsets.kt` (`LocalListBottomInset`, the extra bottom inset for
  a floating mini-player or other persistent bottom UI; defaults to `0.dp`).

## Why depend on this module

Depend on `:common:ui:list-screens` to render a `:common:list` screen. It is the
view half of the pair: `:common:list` produces a `ListStateActual`, this module
draws it. A feature's screen composable picks one of the three renderers based on
its `ColumnType` (one column → `ListScreen`; `Regular`/`Staggered` → `GridScreen`;
drag-reorderable → `ReorderableScreen`), passes its own `itemContent` to draw each
`ListModel`, and lets the screen handle scrolling, insets, and pagination
signalling. The `reorderable` library is an `implementation` detail — `itemContent`
never sees a reorderable type, only a `Modifier` drag handle and an `isDragging`
flag.

## Using it

```kotlin
@Composable
fun SongListScreen(brain: SongListBrain, modifier: Modifier = Modifier) {
    val state by brain.uiStateActual.collectAsState()

    // Reserve space for the floating mini-player below the list.
    CompositionLocalProvider(LocalListBottomInset provides miniPlayerHeight) {
        when (val columns = state.columnType) {
            ColumnType.One -> ListScreen(
                state = state,
                actionSink = brain,        // ListViewModelBrain is an ActionSink
                showDebug = false,
                sideMargin = 16.dp,
                modifier = modifier,
            ) { model, sink, debug, itemMod, padding ->
                ListItem(model, sink, debug, itemMod, padding)
            }

            is ColumnType.Regular -> GridScreen(
                state = state,
                actionSink = brain,
                showDebug = false,
                numberOfColumns = columns.numberOfColumns(WidthClass.MEDIUM),
                staggered = false,
                allowHorizScroller = false,
                sideMargin = 16.dp,
                modifier = modifier,
            ) { model, sink, debug, itemMod, padding ->
                ListItem(model, sink, debug, itemMod, padding)
            }

            is ColumnType.Staggered -> { /* GridScreen(staggered = true, ...) */ }
        }
    }
}
```

`ReorderableScreen` has the same shape but its `itemContent` additionally
receives an `isDragging: Boolean` and a `dragHandle: Modifier` to attach to the
row's drag affordance; a completed drag emits one net `SageAction.Reorder(from, to)`
which the brain's reducer applies.

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js` + `sage.compose.kmp`
- **Targets:** Android + JVM; JS (Node) when built with `-Psage.js` (Compose
  Multiplatform; no `androidMain` code)
- **Source set:** `commonMain`
- **SAGE/module dependencies:** `:common:appcomm`, `:common:list`, `:common:nav`,
  `:common:ui:components` (all `api`); plus `sh.calvin.reorderable` (`implementation`).
