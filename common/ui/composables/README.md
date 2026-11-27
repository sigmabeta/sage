# `:common:ui:composables`

> Shared Compose UI building blocks: `CollageLayout` and the `ListModel` renderer suite.

The home for `@Composable` code that more than one consuming app needs and that isn't a screen
scaffold (those live in `:common:ui:list-screens`). It holds two things:

1. `CollageLayout` — a layout tile for multi-image collages.
2. The **`ListModel` renderer suite** — the built-in `@Composable` renderers for every
   `net.sigmabeta.sage.components.ListModel` type SAGE ships, dispatched from one entry point
   (`ListModel.Content`) and customisable through one CompositionLocal config
   (`ProvideSageRenderers` / `SageRenderers`).

The `ListModel` data types these renderers draw live, Compose-free, in `:common:ui:components`.
Text comes from the app's `StringProvider` (see below); images load through Coil (this module
depends on Coil and the `:common:ui:icons-real` implementation).

## Rendering a list

`ListScreen` and `GridScreen` (in `:common:ui:list-screens`) accept an `itemContent`; the renderer
suite supplies it:

```kotlin
import net.sigmabeta.sage.ui.composables.Content

ListScreen(
    state = listState,
    itemContent = { model, sink, debug, modifier, padding ->
        model.Content(sink, debug, modifier, padding)
    },
)
```

`ListModel.Content` is an extension on `ListModel` that first consults
`LocalSageRenderers.current.contentFor(this)` and falls through to the built-in `when` otherwise.
It tags each row with its model type (for UI-test selection) and wraps it in a perf-measurement
section.

## Customising the renderers

Install both locals at once with `ProvideSageRenderers`:

```kotlin
ProvideSageRenderers(
    stringProvider = myStringProvider,
    renderers = SageRenderers(
        style = SageRendererStyle(
            gridCellCornerRadius = 12.dp,
            heroCornerRadius = 20.dp,
        ),
        contentFor = { model -> /* ... */ null },
        previewImage = { sourceInfo, modifier -> /* ... */ },
    ),
) {
    App()
}
```

### `SageRenderers.style`

Five `Dp` tokens (`SageRendererStyle`) reproduce the renderers' historical pixels by default and
can be tuned per app: `gridCellCornerRadius` (grid cells), `wideItemCornerRadius` (wide-item
thumbnail and shared-transition clip), `heroCornerRadius` (the destination's hero), and
`itemVerticalSpacing` / `cardElevation` (row rhythm and shadow).

### `SageRenderers.contentFor`

`(ListModel) -> ListModelContent?`, consulted **before** the built-ins. Return `null` to fall
through; return a composable to render that model. Use it both for app-owned `ListModel`
subclasses and to replace a built-in renderer. An app row still gets the model-type test tag (on
the `modifier` it is handed) and the perf measurement, exactly like a built-in row, so don't
re-apply the tag yourself.

```kotlin
// An app-owned row, dispatched through the same entry point.
val renderers = SageRenderers(
    contentFor = { model ->
        when (model) {
            is MyFeatureRowModel -> { _, sink, _, modifier, padding ->
                MyFeatureRow(model, sink, modifier, padding)
            }
            else -> null // fall through to the built-ins
        }
    },
)

// Overriding a built-in renderer.
val overridden = SageRenderers(
    contentFor = { model ->
        if (model is WideItemListModel) {
            { m, sink, _, modifier, padding -> MyWideItem(m, sink, modifier, padding) }
        } else null
    },
)
```

### `SageRenderers.previewImage`

Inspection-mode stand-in art. When `null` (default) `FakeImage` — reached from `CrossfadeImage`
under `LocalInspectionMode` — draws a deterministic gradient seeded by the source; an app can plug
its own generated art back in through this hook.

## Strings

The renderers read accessibility labels from `LocalSageStringProvider` (a
`staticCompositionLocalOf<StringProvider>` that errors when absent). The `.text()` / `.text(arg)`
/ `.textInt(arg)` / `.text(first, second)` helpers are on `SageStringId`, so an app's own ids work
with them too.

An app's `StringProvider` must map these eight `SageComponentStringId`s:

| Id | English |
| --- | --- |
| `ACCY_OCL_CHECKBOX` | Toggle checkbox |
| `ACCY_OCL_DROPDOWN` | Expand dropdown |
| `ACCY_OCL_RATING` | Rate |
| `ACCY_OCL_SINGLE_LINE` | Open %1$s |
| `ACCY_OCL_VALUE` | Open |
| `ACCY_ST_DESC_CHECKED` | Checked |
| `ACCY_ST_DESC_LOADING` | Loading |
| `ACCY_ST_DESC_UNCHECKED` | Unchecked |

## Shared elements and the artwork handoff

The image renderers animate one screen's artwork into the next. Four locals drive it, and they stay
per-host/per-screen — an app provides them, they are not part of `SageRenderers`:

- `LocalSharedTransitionScope` (`SharedTransitionScope?`) — the app's one
  `SharedTransitionLayout`, above every tab's navigator.
- `LocalScreenTransitionScope` (`AnimatedVisibilityScope?`) — the active screen's transition slot.
- `LocalArtworkHandoff` (`ArtworkHandoff?`) — the row a destination was opened from (its image
  request and corner), so the destination can draw that bitmap on its first frame.
- `LocalAnimationScale` (`Float`) — the animation-speed multiplier; renderers scale their durations
  by it.

## Platform behaviour

- **Trace sections** — `TraceSection(label) { ... }` wraps each renderer. The Android actual
  delegates to `androidx.tracing.Trace` (Perfetto/systrace); the JVM and JS actuals are no-ops.
- **Crossfade off on JS** — `crossfadeImagesEnabled` is `true` on Android/JVM and `false` on JS,
  where Kotlin/JS's emulated `Long` ops make the per-cell animation slot-table overhead too costly
  during grid scroll.
- **Per-cell offscreen layer** — `gridCellOffscreenLayerEnabled` is on for Skiko (JVM + JS) and off
  for Android, whose hardware renderer already caches each cell's `RenderNode`.

## Contents

| File | What it is |
| --- | --- |
| `CollageLayout.kt` | `CollageLayout(tileCount, modifier, spacing, tile)` — tiles 1–4 slots into one image. Pairs with `ImageCollage` in `:common:images`. |
| `ComposableMapping.kt` | `ListModel.Content` — the built-in renderer dispatch. |
| `SageRenderers.kt` | `SageRenderers` / `SageRendererStyle` / `ListModelContent` and the `ProvideSageRenderers` local. |
| `LocalSageStringProvider.kt` | `LocalSageStringProvider` and the `SageStringId.text*()` helpers. |
| `SharedElements.kt`, `ImageLabelChoreography.kt`, `AnimationScale.kt` | Shared-element / label-choreography plumbing and the animation-scale local. |
| `subs/` | Shared primitives: `CrossfadeImage`, `ElevatedRoundRect`, `MenuActionIcon`, `FakeImage`, `TraceSection`, and the platform `expect`/`actual` gates. |
| `utils/` | `FocusAreaShape`, `ImageSize`, and small layout helpers. |
| `previews/` | Constants the renderers share with their Android `@Preview` fixtures. |

## Using `CollageLayout`

Data code builds an image source with `SourceInfo.ofUrls(urls)` (none / single image /
`ImageCollage`); the app's image composable branches on the collage case:

```kotlin
val info = sourceInfo.info
if (info is ImageCollage) {
    CollageLayout(tileCount = info.sources.size, modifier = modifier) { index, tileModifier ->
        MyImage(SourceInfo(info.sources[index]), tileModifier)
    }
}
```

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js` + `sage.compose.kmp`
- **Targets:** Android + JVM; JS when built with `-Psage.js`
- **Source sets:** `commonMain`, `jvmSharedMain` (`src/main/java`), `androidMain`, `jvmMain`,
  `jsMain`
- **SAGE dependencies:** `:common:images`, `:common:ui:components`, `:common:ui:strings`,
  `:common:appcomm`, `:common:ui:icons-real`, `:common:ui:perf-compose`
