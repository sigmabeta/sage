# `:common:ui:icons-api`

> The `Icon` vocabulary — every icon the UI can name, with no graphics attached.

A single sealed interface enumerating the app's semantic icons (`Play`, `Pause`,
`Search`, `Shuffle`, …). It carries **no** vector assets — those live in
`:common:ui:icons-real`. This api/real split keeps the lightweight icon
*identifiers* free of the heavy Compose `material-icons-extended` dependency.

## Contents

| File | What it is |
| --- | --- |
| `Icon.kt` | `sealed interface Icon` with one `data object` per semantic icon. |

## Why depend on this module

Depend on `:common:ui:icons-api` when code needs to *refer to* an icon — a
ViewModel/state object saying "show the `Play` icon" — without pulling in Compose
or any vector graphics. Only the rendering layer depends on
`:common:ui:icons-real`, which maps each `Icon` to an actual
`ImageVector`. This keeps non-UI and `:api` modules cheap and platform-neutral.

## Using it

```kotlin
// In a state/model type (no Compose dependency needed):
data class ActionButton(val icon: Icon, val label: String)

val playButton = ActionButton(icon = Icon.Play, label = "Play")

// The rendering side (in :common:ui:icons-real) resolves Icon -> ImageVector.
```

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js`
- **Targets:** Android + JVM; JS (Node) when built with `-Pchipbox.js`
- **Source set:** `commonMain`
- **SAGE dependencies:** none (leaf)
- **See also:** `:common:ui:icons-real` (the vector mapping)
