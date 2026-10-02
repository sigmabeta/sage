# `:common:ui:composables`

> Shared, app-agnostic Compose UI building blocks.

The home for `@Composable` code that more than one consuming app needs and that isn't a
screen scaffold (those live in `:common:ui:list-screens`). Everything here is pure Compose
Multiplatform with no theme, resource, or image-loader dependency: composables take their
content as slots, so each app keeps its own look, loading, and error handling.

The `ListModel` data types these pieces often render live, Compose-free, in
`:common:ui:components`.

## Contents

| File | What it is |
| --- | --- |
| `CollageLayout.kt` | `CollageLayout(tileCount, modifier, spacing, tile)` — tiles 1–4 slots into one image: one full tile, two halves, a half plus two quarters, or a 2×2 grid. Each slot is a sized `Box`, so a tile may wrap itself in its own containers (e.g. a `Crossfade`). Pairs with `ImageCollage` in `:common:images`. |

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
- **Source set:** `commonMain`
- **SAGE dependencies:** `:common:images`
