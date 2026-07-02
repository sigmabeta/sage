# `:common:ui:icons-real`

> Maps the platform-agnostic `Icon` sealed interface to concrete Compose `ImageVector`s.

The `:real` half of the icons abstraction: `:common:ui:icons-api` declares the
`Icon` sealed interface (a `data object` per logical icon) with no Compose dependency,
and this module supplies the actual `ImageVector` for each via the `Icon.vector()`
extension. Most icons resolve to `Icons.Default.*`/`Icons.AutoMirrored.Default.*` from
`material-icons-extended`; a handful that have no Material equivalent are hand-built
vector assets under `SageMaterialVectors`.

## Contents

- **Mapping** — `Icon.kt` defines `fun Icon.vector(): ImageVector`, an exhaustive
  `when` over every `Icon` subtype returning the matching `ImageVector`.
- **Custom vector namespace** — `SageMaterialVectors.kt` is an empty `object`
  namespace; each `icons/Ic*24dp.kt` file adds a lazily-built `internal` extension
  `ImageVector` (or builder function) on it, e.g. `IcAlbum24dp`, `IcBarChart24dp`,
  `IcCloudDone24dp`, `IcCrossOut*24dp`, `IcDescription24dp`, `IcFavoriteEmpty/Filled`,
  `IcPlayCircleFilled24`, `IcRemove24dp`, `IcTagBlack24dp`, plus the imported
  `IcOutlineCloudDownload24dp`. These cover the icons with no Material counterpart.

## Why depend on this module

Depend on `:common:ui:icons-api` for the `Icon` type (it stays Compose-free, so UI
state/model code can name an icon without importing Compose). Depend on
`:common:ui:icons-real` only where you actually render — typically the appui layer that
turns an `Icon` from a `ListModel` into a Compose `Icon(imageVector = ...)`. Keeping the
mapping here means the `:api` side, and everything that depends on it, never pulls in
`material-icons-extended`.

## Using it

```kotlin
@Composable
fun rememberIconVector(icon: Icon): ImageVector = icon.vector()

// e.g. rendering a model that carries an Icon placeholder:
Icon(imageVector = model.imagePlaceholder.vector(), contentDescription = null)
```

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js` + `sage.compose.kmp`
- **Targets:** Android + JVM; JS (Node) when built with `-Psage.js`
- **Source set:** `commonMain`
- **SAGE/module dependencies:** `:common:ui:icons-api` (`implementation`);
  `jetbrains-compose-material-icons-extended` (`api`)
