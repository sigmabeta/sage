# `:common:ui:component-library`

> A drop-in debug component gallery: a pure `ListModel` catalog, a reducer for its live controls, and
> a tiny style host.

Any SAGE app can wire this into its debug menu to see every built-in renderer in the list, grid and
detail-style (staggered) layouts, tweak the five `SageRendererStyle` tokens live, and see the loading,
empty and error states. Everything except `ComponentLibraryHost` is Compose-free (no `Dp`, no
`@Composable`), so plain KMP feature modules can hold the config in their state.

## Integration recipe

```kotlin
// 1. State: hold the config; report the layout's columns; return the catalog.
data class GalleryState(val config: ComponentLibraryConfig = ComponentLibraryConfig()) : ListState() {
    override val columnType = config.layout.columnType
    override fun toListItems(stringProvider: StringProvider) = componentLibraryItems(config, extraSamples = myRows)
}

// 2. ViewModel: forward gallery actions to the pure reducer. `updateState` stands for whatever
//    state-update helper your base ViewModel provides; the name is yours.
override fun handleAction(action: SageAction) {
    when (action) {
        is ComponentLibraryAction -> updateState { it.copy(config = it.config.reduce(action)) }
        else -> Unit
    }
}

// 3. Screen: collect the ViewModel's rendered ListStateActual (every SAGE list VM exposes one) and
//    wrap the app's list screen.
val state by viewModel.uiStateActual.collectAsState()
ComponentLibraryHost(state.listItems) { MyListScreen(viewModel) }
```

No typed state flow is needed: `componentLibraryItems` appends the live style as the last item, an
invisible `ComponentLibraryStyleListModel` carrier row, and `ComponentLibraryHost(items)` reads it
back with `componentLibraryStyleOf(items)`. (If you hold the config directly, use the
`ComponentLibraryHost(style)` overload.) The carrier renders as a zero-size `Spacer`; note that
`GridScreen`'s fixed-column grid adds its 8 dp vertical gap before it, so the grid layout ends with
that much extra space.

## Ids

- The catalog's explicit ids live in `1_000_000..1_999_999` (the style carrier is `1_000_999`).
- Build ids for your own rows with `componentLibraryExtraId(index)` (`2_000_000 + index`); they
  can't collide with the catalog's explicit ids.
- Some catalog ids are hash-derived from text (image cells, dropdown setting ids) and can't be
  reserved: don't reuse the catalog's section titles or `cl-` setting ids.
- `componentLibraryItems` walks the whole tree (including scroller, section and subsection
  children) and throws `IllegalStateException`, naming each duplicate id and its models, if any
  `dataId` repeats. `ListState.toActual` catches exceptions from `toListItems` and shows an error
  row, so in an app a collision is a visible error; in a unit test calling `componentLibraryItems`
  directly it is a thrown exception.

## App-specific rows

Pass your own `ListModel`s as `extraSamples`; they follow the catalog under an "App rows" header,
unchanged. They render through your `SageRenderers.contentFor`, which `ComponentLibraryHost` preserves.
Give them `dataId`s from `componentLibraryExtraId` (see Ids above).

## Why the labels are English literals

This is a debug-only developer tool, like chipbox's gallery it was ported from, so its labels
("Controls", "Grid cell corner: 16 dp", ...) are plain English and add no `StringId`s to your string
table. Don't ship it in a user-facing surface.

## Notes

- The sample text comes from `componentLibraryContent(seed)`: `StringGenerator` on JVM/Android,
  deterministic placeholders on JS. Pass the same seed for the same text.
- `ComponentLibraryHost` forces `LocalInspectionMode = true`, so the catalog's synthetic `SourceInfo`s
  draw preview art instead of attempting image loads.
