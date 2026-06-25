# `:android:ui:themes`

> Material 3 theme wrappers for the Android app.

Thin Compose helpers that wrap `MaterialTheme` with SAGE's light/dark colour
selection. Android-only — this is the platform glue that brings Compose Material 3
and the splash-screen library into the app; it has no SAGE module dependencies.

## Contents

| File | What it is |
| --- | --- |
| `SageMaterial.kt` | `SageMaterial(lightColors, darkColors, typography, forceDark, content)` — picks light/dark `ColorScheme` from the system setting (or forces dark) and applies `MaterialTheme`. Plus `SageMaterialMenu(menuColors, typography, content)` for menu surfaces that always use a fixed scheme. |

## Why depend on this module

Depend on `:android:ui:themes` from the Android app/UI layer to wrap your content
in the app theme. It also re-exports (`api`) the Compose BOM, Material 3,
`material`, and the splash-screen artifact, so consuming it brings the Material 3
toolkit along. Callers supply their own colour schemes and typography; this
module only handles the light/dark switch and `MaterialTheme` application.

## Using it

```kotlin
setContent {
    SageMaterial(
        lightColors = ChipboxLightColors,
        darkColors = ChipboxDarkColors,
        typography = ChipboxTypography,
    ) {
        App()
    }
}
```

## Module facts

- **Plugin:** `sage.android` + `compose.compiler`
- **Targets:** Android only
- **Source set:** `src/main/java`
- **SAGE dependencies:** none — exposes Material 3 / Compose BOM / splash via `api`
