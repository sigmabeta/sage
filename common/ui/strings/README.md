# `:common:ui:strings`

> Platform-agnostic string lookup — the `StringProvider`/`SageStringId` indirection plus a placeholder-text generator.

The contract for resolving localized strings from common code without touching
platform resource APIs: an opaque `SageStringId` key type and a `StringProvider`
that turns a key (with optional arguments) into a `String`. It's `:api`-style —
the resource-backed implementation lives elsewhere and is wired via DI. Also
ships `StringGenerator`, a deterministic lorem/name/title generator for
fakes, previews, and screenshot data.

## Contents

| File | What it is |
| --- | --- |
| `SageStringId.kt` | Marker `interface SageStringId` — the opaque key each platform's string enum implements. |
| `StringProvider.kt` | `interface StringProvider`: `getString` plus `getStringOneArg` / `getStringOneInt` / `getStringTwoArgs` formatting variants. |
| `StringGenerator.kt` | Seeded-`Random` generator of fake names, titles, and lorem text (`generateName`/`generateTitle`/`generateLorem`). Uses `java.util.*`, so it lives in `jvmSharedMain` (`src/main/java`). |

## Why depend on this module

Depend on `:common:ui:strings` when common/feature code needs to display
user-facing text but must stay multiplatform — reference `StringProvider` and
`SageStringId` instead of platform resources, and let the app inject the real
provider. Pull it in for `StringGenerator` when building fakes or screenshot
fixtures that need plausible placeholder text.

## Using it

```kotlin
class Greeter(private val strings: StringProvider) {
    fun greeting(name: String): String =
        strings.getStringOneArg(MyStrings.GREETING, name)
}

// Fake/preview data:
val fakeTitle = StringGenerator(Random(42)).generateTitle()
```

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js`
- **Targets:** Android + JVM; JS (Node) when built with `-Psage.js`
- **Source set:** `commonMain` (interfaces) + `src/main/java` (`jvmSharedMain`, `StringGenerator`)
- **SAGE/module dependencies:** `:common:connectivity`
