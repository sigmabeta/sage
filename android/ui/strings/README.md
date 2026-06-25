# `:android:ui:strings`

> Android `StringProvider` — resolves `SageStringId`s to Android string resources.

The Android-only implementation of `common:ui:strings`'s `StringProvider`. It
maps SAGE's platform-neutral `SageStringId` keys to Android string-resource IDs
and formats them via `android.content.res.Resources`.

## Contents

| File | What it is |
| --- | --- |
| `AndroidStringProvider.kt` | `StringProvider` impl. Constructed with `Resources` and a `(SageStringId) -> Int` lookup; implements `getString`, `getStringOneArg`, `getStringOneInt`, and `getStringTwoArgs` by resolving the ID and calling `Resources.getString` with the args. |

## Why depend on this module

Depend on `:common:ui:strings` for the `SageStringId`/`StringProvider` types;
include `:android:ui:strings` in the Android app to resolve those IDs against the
app's actual string resources. There are no DI bindings here — the app supplies
the `Resources` and the `SageStringId -> Int` mapping (which lives with the app's
generated `R` resources) and binds `AndroidStringProvider` as the
`StringProvider`.

## Using it

```kotlin
val strings: StringProvider = AndroidStringProvider(
    resources = context.resources,
    toResourceId = { id -> sageStringIdToResId(id) },
)
strings.getStringOneArg(SageStringId.NowPlaying, trackTitle)
```

## Module facts

- **Plugin:** `sage.android`
- **Targets:** Android only
- **Source set:** `src/main/java`
- **SAGE/module dependencies:** `api(projects.common.ui.strings)`
