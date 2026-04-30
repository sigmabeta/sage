# SAGE — Sigmabeta App Generalization Engine

Reusable Android infrastructure for sigmabeta apps. Consumed via Gradle composite build — no Maven publication required.

## Requirements

| Tool | Version |
|------|---------|
| JDK | 17 |
| Gradle | 8.14.2 |
| Android Gradle Plugin | 8.11.0 |
| Kotlin | 2.2.0 |
| compileSdk / targetSdk | 35 / 36 |
| minSdk | 21 |

## Module Inventory

### `common/` — pure JVM/Kotlin

| Module | Coordinates | Description |
|--------|-------------|-------------|
| `analytics` | `net.sigmabeta.sage:common-analytics` | Analytics event abstraction |
| `appcomm` | `net.sigmabeta.sage:common-appcomm` | App-wide communication bus (Moshi + KSP) |
| `appinfo` | `net.sigmabeta.sage:common-appinfo` | App version / build info |
| `connectivity` | `net.sigmabeta.sage:common-connectivity` | Network connectivity state |
| `coroutines` | `net.sigmabeta.sage:common-coroutines` | Coroutine dispatcher providers |
| `debug` | `net.sigmabeta.sage:common-debug` | Debug-only utilities |
| `events` | `net.sigmabeta.sage:common-events` | Event dispatcher |
| `images` | `net.sigmabeta.sage:common-images` | Image loading abstractions |
| `list` | `net.sigmabeta.sage:common-list` | List model types |
| `logging` | `net.sigmabeta.sage:common-logging` | `Hatchet` logging abstraction |
| `nav` | `net.sigmabeta.sage:common-nav` | Navigation destination types |
| `pdf` | `net.sigmabeta.sage:common-pdf` | PDF config model types |
| `perf` | `net.sigmabeta.sage:common-perf` | Performance monitoring abstraction |
| `settings/general` | `net.sigmabeta.sage:common-settings-general` | General settings model |
| `settings/environment` | `net.sigmabeta.sage:common-settings-environment` | Environment/server settings |
| `storage/common` | `net.sigmabeta.sage:common-storage-common` | Storage abstractions |
| `time` | `net.sigmabeta.sage:common-time` | Time/date utilities (ThreeTen) |
| `ui/components` | `net.sigmabeta.sage:common-ui-components` | Shared UI component model types |
| `ui/icons` | `net.sigmabeta.sage:common-ui-icons` | Icon abstractions |
| `ui/strings` | `net.sigmabeta.sage:common-ui-strings` | String resource abstractions |
| `wakelocks` | `net.sigmabeta.sage:common-wakelocks` | Wakelock abstraction |

### `android/` — Android library modules

| Module | Coordinates | Description |
|--------|-------------|-------------|
| `analytics` | `net.sigmabeta.sage.android:android-analytics` | Firebase/analytics implementation |
| `bitmaps` | `net.sigmabeta.sage.android:android-bitmaps` | Bitmap generation (loading indicators, PDF thumbnails) |
| `connectivity` | `net.sigmabeta.sage.android:android-connectivity` | Android connectivity implementation |
| `coroutines` | `net.sigmabeta.sage.android:android-coroutines` | Android coroutine dispatcher impl |
| `firebase` | `net.sigmabeta.sage.android:android-firebase` | Firebase setup |
| `logging` | `net.sigmabeta.sage.android:android-logging` | `AndroidHatchet` implementation |
| `perf` | `net.sigmabeta.sage.android:android-perf` | Compose performance monitoring |
| `resources` | `net.sigmabeta.sage.android:android-resources` | Android resource utilities |
| `ui/colors` | `net.sigmabeta.sage.android:android-ui-colors` | Color tokens |
| `ui/fonts` | `net.sigmabeta.sage.android:android-ui-fonts` | Font tokens (MuseJazz) |
| `ui/icons` | `net.sigmabeta.sage.android:android-ui-icons` | Icon implementations |
| `ui/strings` | `net.sigmabeta.sage.android:android-ui-strings` | String resource implementations |
| `ui/themes` | `net.sigmabeta.sage.android:android-ui-themes` | `VglsMaterial` Compose theme |
| `wakelocks` | `net.sigmabeta.sage.android:android-wakelocks` | Android wakelock implementation |

### `fake/` — test doubles

| Module | Coordinates | Description |
|--------|-------------|-------------|
| `analytics` | `net.sigmabeta.sage.fake:fake-analytics` | No-op analytics (Hilt module) |
| `perf` | `net.sigmabeta.sage.fake:fake-perf` | No-op performance monitoring |

## Convention Plugins

Defined in `sage-build-logic/`. All plugins auto-apply detekt and configure JVM 17.

| Plugin ID | Use for | Auto-deps |
|-----------|---------|-----------|
| `sage.android` | Android library modules | `kotlin-stdlib`, `common-logging`, core library desugaring |
| `sage.jvm` | Pure Kotlin/JVM modules | `kotlin-stdlib` |
| `sage.compose.android` | Android library modules with Compose | same as `sage.android` + Compose compiler |

### What `sage.android` configures

- `com.android.library` + `org.jetbrains.kotlin.android` + `io.gitlab.arturbosch.detekt`
- compileSdk 35, targetSdk 36, minSdk 21
- JVM 17 source/target compatibility
- Core library desugaring enabled
- `detekt-config.yml` from repo root

## How to Consume SAGE

In your app's `settings.gradle.kts`:

```kotlin
// 1. Plugin resolution — must be top-level (not inside pluginManagement)
includeBuild("../sage/sage-build-logic")

// 2. Library modules — explicit substitution rules required because SAGE uses
//    archivesName for stable IDs rather than project.name
includeBuild("../sage") {
    dependencySubstitution {
        substitute(module("net.sigmabeta.sage:common-logging")).using(project(":common:logging"))
        substitute(module("net.sigmabeta.sage:android-ui-themes")).using(project(":android:ui:themes"))
        // ... one entry per SAGE module you depend on
    }
}

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    versionCatalogs {
        create("libs") {
            from(files("../sage/gradle/libs.versions.toml"))
        }
    }
    repositories {
        google()
        mavenCentral()
    }
}
```

In your root `build.gradle.kts`, declare (but don't apply) the plugins needed by `sage.android` so their JARs land on the build-script classpath:

```kotlin
plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kotlin.jvm) apply false
}
```

In a module's `build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.sage.android)
}

android {
    namespace = "com.example.mymodule"
}

dependencies {
    implementation(libs.sage.common.logging)
    implementation(libs.sage.android.ui.themes)
}
```

> **Note:** SAGE imports the shared version catalog, so you get all library version aliases
> (`libs.hilt`, `libs.androidx.compose.bom`, etc.) without maintaining a separate TOML file.

See `/home/sigma/projects/android/sage-smoke-test` for a minimal working reference.

## Standalone Build

```bash
./gradlew help
```

## Gradle composite build notes

- `sage-build-logic/` is intentionally **not** named `build-logic/` to avoid a Gradle composite build path collision when both the consumer and SAGE use `build-logic/` as their own build-logic directory.
- Each top-level namespace uses a distinct Maven group so modules sharing the same `project.name` across namespaces (e.g. `:common:perf` and `:fake:perf`) don't cause module-identity collisions during dependency resolution.
