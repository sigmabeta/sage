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

// 2. ViewModel: forward gallery actions to the pure reducer.
override fun handleAction(action: SageAction) = when (action) {
    is ComponentLibraryAction -> update { copy(config = config.reduce(action)) }
    else -> super.handleAction(action)
}

// 3. Route: wrap the list so the style tweaks apply and previews render art.
ComponentLibraryHost(style = state.config.style) { ListScreen(state, itemContent = { m, s, d, mod, p -> m.Content(s, d, mod, p) }) }
```

## App-specific rows

Pass your own `ListModel`s as `extraSamples`; they follow the catalog under an "App rows" header,
unchanged. They render through your `SageRenderers.contentFor`, which `ComponentLibraryHost` preserves.
Give them `dataId`s that don't collide with each other (SAGE throws on duplicates at render time).

## Why the labels are English literals

This is a debug-only developer tool, like chipbox's gallery it was ported from, so its labels
("Controls", "Grid cell corner: 16 dp", ...) are plain English and add no `StringId`s to your string
table. Don't ship it in a user-facing surface.

## Notes

- The sample text comes from `componentLibraryContent(seed)`: `StringGenerator` on JVM/Android,
  deterministic placeholders on JS. Pass the same seed for the same text.
- `ComponentLibraryHost` forces `LocalInspectionMode = true`, so the catalog's synthetic `SourceInfo`s
  draw preview art instead of attempting image loads.
