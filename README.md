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
| `analytics` | `net.sigmabeta.sage:analytics` | Analytics event abstraction |
| `appcomm` | `net.sigmabeta.sage:appcomm` | App-wide communication bus (Moshi + KSP) |
| `appinfo` | `net.sigmabeta.sage:appinfo` | App version / build info |
| `connectivity` | `net.sigmabeta.sage:connectivity` | Network connectivity state |
| `coroutines` | `net.sigmabeta.sage:coroutines` | Coroutine dispatcher providers |
| `debug` | `net.sigmabeta.sage:debug` | Debug-only utilities |
| `events` | `net.sigmabeta.sage:events` | Event dispatcher |
| `images` | `net.sigmabeta.sage:images` | Image loading abstractions |
| `list` | `net.sigmabeta.sage:list` | List model types |
| `logging` | `net.sigmabeta.sage:logging` | `Hatchet` logging abstraction |
| `nav` | `net.sigmabeta.sage:nav` | Navigation destination types |
| `pdf` | `net.sigmabeta.sage:pdf` | PDF config model types |
| `perf` | `net.sigmabeta.sage:perf` | Performance monitoring abstraction |
| `settings/general` | `net.sigmabeta.sage:general` | General settings model |
| `settings/environment` | `net.sigmabeta.sage:environment` | Environment/server settings |
| `storage/common` | `net.sigmabeta.sage:common` | Storage abstractions |
| `time` | `net.sigmabeta.sage:time` | Time/date utilities (ThreeTen) |
| `ui/components` | `net.sigmabeta.sage:components` | Shared UI component model types |
| `ui/icons` | `net.sigmabeta.sage:icons` | Icon abstractions |
| `ui/strings` | `net.sigmabeta.sage:strings` | String resource abstractions |

### `android/` — Android library modules

| Module | Coordinates | Description |
|--------|-------------|-------------|
| `analytics` | `net.sigmabeta.sage.android:analytics` | Firebase/analytics implementation |
| `bitmaps` | `net.sigmabeta.sage.android:bitmaps` | Bitmap generation (loading indicators, PDF thumbnails) |
| `connectivity` | `net.sigmabeta.sage.android:connectivity` | Android connectivity implementation |
| `coroutines` | `net.sigmabeta.sage.android:coroutines` | Android coroutine dispatcher impl |
| `firebase` | `net.sigmabeta.sage.android:firebase` | Firebase setup |
| `logging` | `net.sigmabeta.sage.android:logging` | `AndroidHatchet` implementation |
| `perf` | `net.sigmabeta.sage.android:perf` | Compose performance monitoring |
| `resources` | `net.sigmabeta.sage.android:resources` | Android resource utilities |
| `ui/icons` | `net.sigmabeta.sage.android:icons` | Icon implementations |
| `ui/themes` | `net.sigmabeta.sage.android:themes` | `SageMaterial` / `SageMaterialMenu` composables — accepts `lightColors`, `darkColors`, and `typography` from the caller |

### `fake/` — test doubles

| Module | Coordinates | Description |
|--------|-------------|-------------|
| `analytics` | `net.sigmabeta.sage.fake:analytics` | No-op analytics (Hilt module) |
| `perf` | `net.sigmabeta.sage.fake:perf` | No-op performance monitoring |

## Convention Plugins

Defined in `sage-build-logic/`. All plugins auto-apply detekt and configure JVM 17.

| Plugin ID | Use for | Auto-deps |
|-----------|---------|-----------|
| `sage.android` | Android library modules | `kotlin-stdlib`, `logging`, core library desugaring |
| `sage.jvm` | Pure Kotlin/JVM modules | `kotlin-stdlib` |
| `sage.compose.android` | Android library modules with Compose | same as `sage.android` + Compose compiler |

### What `sage.android` configures

- `com.android.library` + `org.jetbrains.kotlin.android` + `io.gitlab.arturbosch.detekt`
- compileSdk 35, targetSdk 36, minSdk 21
- JVM 17 source/target compatibility
- Core library desugaring enabled
- `detekt-config.yml` from repo root

## How to Consume SAGE

### 1. Add SAGE as a submodule

```bash
git submodule add git@github.com:sigmabeta/sage.git sage
```

This pins the consumer repo to a specific SAGE commit. To update SAGE later:

```bash
git -C sage pull
git add sage
git commit -m "Bump SAGE"
```

Anyone cloning the consumer repo needs to initialize the submodule:

```bash
git clone --recurse-submodules <repo-url>
# or, after a plain clone:
git submodule update --init --recursive
```

### 2. Wire into Gradle

In `settings.gradle.kts` (paths are relative to the submodule directory `sage/`):

```kotlin
// Plugin resolution — must be top-level (not inside pluginManagement)
includeBuild("sage/sage-build-logic")

// Library modules — auto-substitution handles wiring; no explicit rules needed
includeBuild("sage")

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
            from(files("sage/gradle/libs.versions.toml"))
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

### 3. CI setup

After `checkout`, initialize the submodule before running Gradle:

```bash
git submodule sync && git submodule update --init --recursive
```

See `/home/sigma/projects/android/sage-smoke-test` for a minimal working reference.

## Standalone Build

```bash
./gradlew help
```

## Gradle composite build notes

- `sage-build-logic/` is intentionally **not** named `build-logic/` to avoid a Gradle composite build path collision when both the consumer and SAGE use `build-logic/` as their own build-logic directory.
- Each top-level namespace uses a distinct Maven group so modules sharing the same `project.name` across namespaces (e.g. `:common:perf` and `:fake:perf`) don't cause module-identity collisions during dependency resolution.
- Module coordinates follow Gradle's auto-substitution formula: `group:project.name` (last path segment). The root `build.gradle.kts` assigns groups only to projects with a build file, preventing virtual parent directories (`:common`, `:android:ui`, etc.) from registering conflicting coordinates.
