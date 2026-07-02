# `:common:freeform`

> Base `SageState` for screens that build their own content model instead of a flat list.

A small base layer for "freeform" screens — ones whose rendered output isn't a list of
`ListModel`s but an arbitrary, screen-defined `Model`. `FreeformState<Model>` turns the
raw state into a title + content pair, wrapping `title`/`toContent` in try/catch so a
thrown error becomes an `errorContent(...)` model rather than a crash. These are data
types, not Composables; the module applies no Compose plugin.

## Contents

| File | What it is |
| --- | --- |
| `FreeformState.kt` | Abstract `FreeformState<Model> : SageState`. Subclasses implement `title(StringProvider)`, `toContent(StringProvider)`, and `errorContent(Throwable)`; `toActual(...)` composes them into a `FreeformStateActual`, swallowing exceptions into a fallback title / `errorContent`. Also provides a protected `LCE<T>.fold(...)` helper for branching on uninitialized/loading/error/content. |
| `FreeformStateActual.kt` | `data class FreeformStateActual<Model>(title: TitleBarModel, content: Model)` — the resolved, render-ready output of `toActual`. |

## Why depend on this module

Depend on `:common:freeform` when a feature's screen needs a custom content model rather
than the shared `ListModel` list vocabulary, but still wants the standard SAGE state
contract (a `TitleBarModel` title bar plus error-safe content resolution). It sits
alongside the list-based path: list screens use `:common:ui:components` directly,
freeform screens extend `FreeformState<Model>` here.

## Using it

```kotlin
data class NowPlayingState(val track: LCE<Track>) : FreeformState<NowPlayingModel>() {
    override fun title(stringProvider: StringProvider) =
        TitleBarModel(title = stringProvider.get(/* ... */))

    override fun toContent(stringProvider: StringProvider): NowPlayingModel =
        track.fold(
            onUninitialized = { NowPlayingModel.Empty },
            onLoading = { NowPlayingModel.Loading },
            onError = { _, e -> errorContent(e) },
            onContent = { NowPlayingModel.Loaded(it) },
        )

    override fun errorContent(error: Throwable) = NowPlayingModel.Error(error.message)
}

// In the screen: val actual = state.toActual(stringProvider)
```

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js`
- **Targets:** Android + JVM; JS (Node) when built with `-Psage.js`
- **Source set:** `commonMain`
- **SAGE/module dependencies (all `api`):** `:common:appcomm`, `:common:ui:strings`,
  `:common:ui:components`
