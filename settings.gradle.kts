includeBuild("sage-build-logic")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.develocity") version "4.4.2"
}

develocity {
    buildScan {
        // Free public Build Scan service (scans.gradle.com) — accept its terms non-interactively.
        termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
        termsOfUseAgree = "yes"

        // Auto-publish on CI only; locally publish on demand with `--scan` (which overrides
        // this predicate). Reading via providers keeps it configuration-cache safe.
        val isCi = providers.environmentVariable("CI").isPresent
        publishing.onlyIf { isCi }
        if (isCi) tag("CI")
    }
}

buildCache {
    local {
        directory = File(rootDir, "build-cache")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "SAGE"

include(
    ":common:analytics",
    ":common:appcomm",
    ":common:appinfo",
    ":common:connectivity",
    ":common:coroutines",
    ":common:debug",
    ":common:di",
    ":common:events",
    ":common:freeform",
    ":common:images",
    ":common:list",
    ":common:logging",
    ":common:nav",
    ":common:pdf",
    ":common:perf",
    ":common:settings:environment",
    ":common:settings:general",
    ":common:storage:common",
    ":common:time",
    ":common:ui:components",
    ":common:ui:icons-api",
    ":common:ui:icons-real",
    ":common:ui:perf-compose",
    ":common:ui:list-screens",
    ":common:ui:strings",
    ":android:analytics",
    ":android:connectivity",
    ":android:coroutines",
    ":android:firebase",
    ":android:logging",
    ":android:resources",
    ":android:ui:strings",
    ":android:ui:themes",

    ":fake:analytics",
    ":fake:perf",
)
