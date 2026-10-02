# `:common:images`

> Shared value types for image/PDF rendering — no rendering code.

Small platform-agnostic types that let common code talk about image sources
and target sizes while the actual decoding lives in platform modules. A leaf
module.

## Contents

| File | What it is |
| --- | --- |
| `PdfSize.kt` | `enum PdfSize { THUMBNAIL, MEDIUM, LARGE, FILL }` — the size bucket to render a page/cover at. |
| `SourceInfo.kt` | `data class SourceInfo(info: Any?)` — an opaque, platform-erased handle to an image source. The `Any?` is deliberately untyped so `commonMain` can pass a source token across the expect/actual boundary without naming a platform type. `SourceInfo.ofUrls(urls)` builds one from 0..n URLs: no image, the single URL, or an `ImageCollage`. |
| `ImageCollage.kt` | `data class ImageCollage(sources: List<String>)` — 2–4 image URLs to show as one tiled image (rendered with `:common:ui:composables`'s `CollageLayout`). |

## Why depend on this module

Depend on `:common:images` when common UI/data code needs to *describe* an image
request — "render this source at `THUMBNAIL`" — without depending on a platform
image loader. The concrete loader (Coil on Android, etc.) interprets `SourceInfo`
and `PdfSize` on the platform side. `:common:pdf` and `:common:ui:components`
build on these types.

## Using it

```kotlin
data class CoverRequest(val source: SourceInfo, val size: PdfSize)

// Common code constructs the request; a platform renderer consumes it:
CoverRequest(source = SourceInfo(fileHandle), size = PdfSize.MEDIUM)
```

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js`
- **Targets:** Android + JVM; JS (Node) when built with `-Psage.js`
- **Source set:** `commonMain`
- **SAGE dependencies:** none (leaf)
