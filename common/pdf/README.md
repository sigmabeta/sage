# `:common:pdf`

> A single value type, `PdfConfigById`, describing how to render a song's PDF sheet music page.

A tiny types-only module holding `PdfConfigById`, the request descriptor that
identifies which PDF (by song id), which page, which variant, and at what size a
renderer should produce a sheet-music image. It depends on `:common:images` only
for the `PdfSize` enum; the actual rendering lives elsewhere.

## Contents

| File | What it is |
| --- | --- |
| `PdfConfigById.kt` | `data class PdfConfigById(songId, pageNumber?, isAltSelected, pdfSize, maxWidth?, maxHeight?)` — addresses a PDF page for a given song, selects the alternate document, and bounds the rendered size. `pdfSize` is `:common:images`' `PdfSize`. |

## Why depend on this module

Depend on `:common:pdf` when code needs to describe or pass around a PDF render
request — UI requesting a page, an image loader keyed on PDF config, or a cache
keyed by it. It's a leaf type carrier with no behavior, so depending on it costs
nothing beyond the `PdfSize` enum it reuses.

## Using it

```kotlin
val config = PdfConfigById(
    songId = song.id,
    pageNumber = 0,
    isAltSelected = false,
    pdfSize = PdfSize.FULL,
    maxWidth = 1080,
)
// hand `config` to the PDF image loader / renderer
```

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js`
- **Targets:** Android + JVM; JS (Node) when built with `-Pchipbox.js`
- **Source set:** `commonMain`
- **SAGE/module dependencies:** `:common:images`
