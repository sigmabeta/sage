# `:common:appinfo`

> The `AppInfo` value type describing the running build.

A single data class holding build identity — debug flag, version, build time,
branch. A leaf module with no dependencies at all.

## Contents

| File | What it is |
| --- | --- |
| `AppInfo.kt` | `data class AppInfo(isDebug, versionName, versionCode, buildTimeMs, buildBranch)`. |

## Why depend on this module

Depend on `:common:appinfo` when a module needs to know *which* build it is
running in — to gate debug-only behaviour (`isDebug`), show a version string, or
report `buildBranch` to analytics — without reaching for Android's
`BuildConfig` (which doesn't exist on JVM/JS and would tie the module to one
platform). A platform module constructs the `AppInfo` instance from its own
build constants and provides it through DI; common code just consumes the type.

## Using it

```kotlin
class AboutViewModel(appInfo: AppInfo) {
    val versionLabel = "v${appInfo.versionName} (${appInfo.versionCode})"
    val showDebugMenu = appInfo.isDebug
}
```

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js`
- **Targets:** Android + JVM; JS (Node) when built with `-Psage.js`
- **Source set:** `commonMain`
- **SAGE dependencies:** none (leaf)
