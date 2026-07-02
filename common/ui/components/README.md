# `:common:ui:components`

> The shared `ListModel` vocabulary — renderer-agnostic UI item descriptions.

The catalogue of `ListModel` data types that screens emit and the appui layer renders.
Each model is a plain, immutable description of one list row/cell (text, image, CTA,
section, settings control, …) carrying a stable `dataId`, a `columns` span, and its
click/edit `SageAction`s. These are **data types, not Composables** — this module holds
no `@Composable` code (it doesn't even apply the Compose plugin); the actual rendering
lives in the consuming UI layer.

## Contents

- **Base type** — `ListModel.kt` is the abstract base every model extends: `dataId`,
  `columns` (with the `COLUMNS_ALL = -1` sentinel), and an open `layoutId()` used as the
  LazyList `contentType`/layout-identity key. It's intentionally `open`, not `sealed`, so
  downstream features can add their own renderers.
- **Layout identity** — `RuntimeClassName.kt` declares `expect fun runtimeClassName`,
  with `actual`s in `src/main/java` (`javaClass.name`, JVM/Android) and `src/jsMain`
  (simple name, JS); it backs `ListModel.layoutId()`.
- **Text & label rows** — `SingleTextListModel`, `SmallTextListModel`,
  `NameCaption*`/`NameCaptionValue*`, `LabelValueListModel`, `LabelRatingStarListModel`,
  `SectionHeaderListModel`, `SubsectionHeaderListModel`.
- **Icon / image rows & grids** — `IconName*`, `Image/ImageNameCaption*`,
  `GridImageListModel` (+ `GridImageSize`), `WideItemListModel`, `HeroImageListModel`.
- **Settings & interactive controls** — `CheckableListModel`,
  `NameCaptionCheckboxListModel`, `DropdownSettingListModel` (with `ofLabels` helper),
  `EditTextListModel`, `ConfirmationListModel`, `CtaListModel`.
- **Containers & sections** — `SectionListModel`, `SubsectionListModel`,
  `HorizontalScrollerListModel`, `CollapsibleDetailsListModel`.
- **State / status rows** — `EmptyStateListModel`, `ErrorStateListModel`,
  `LoadingItemListModel` (+ `LoadingType`), `NotifListModel`,
  `NetworkRefreshingListModel`, `NoopListModel` (a filtered-out sentinel that throws if
  rendered).
- **Search** — `SearchHistoryListModel`, `SearchResultListModel`.
- **Sheet-music / PDF** — `SheetPageListModel`, `SheetPageCardListModel`,
  `SinglePageListModel`, `ZoomableSheetPageListModel`.
- **Screen chrome** — `TitleBarModel` (title/subtitle/back).

## Why depend on this module

Depend on `:common:ui:components` whenever a feature builds list state — its
`ChipboxListViewModel`/`ListState` produces `ImmutableList<ListModel>` from these types,
and the appui renderer maps them to Compose. It's the lingua franca between feature state
and the renderer, so both sides depend on it. Because `ListModel` is open, a feature can
also subclass it for a bespoke row without modifying this module.

## Using it

```kotlin
val items: ImmutableList<ListModel> = persistentListOf(
    SectionHeaderListModel(title = "Recently played"),
    ImageNameCaptionListModel(
        dataId = game.id,
        name = game.title,
        caption = game.platform,
        sourceInfo = game.cover,
        imagePlaceholder = Icon.Album,
        clickAction = SageAction.Navigate(/* ... */),
    ),
    CtaListModel(icon = Icon.Plus, name = "Add to playlist", clickAction = /* ... */),
)
```

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js`
- **Targets:** Android + JVM; JS (Node) when built with `-Psage.js`
- **Source set:** `commonMain` (with `src/main/java` = `jvmSharedMain` and `src/jsMain`
  `actual`s for `runtimeClassName`)
- **SAGE/module dependencies (all `api`):** `:common:appcomm`, `:common:images`,
  `:common:pdf`, `:common:ui:icons-api`; `kotlinx-collections-immutable`
