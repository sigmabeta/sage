includeBuild("sage-build-logic")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.develocity") version "3.17.3"
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
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
    ":common:ui:icons",
    ":common:ui:strings",
    ":android:analytics",
    ":android:bitmaps",
    ":android:connectivity",
    ":android:coroutines",
    ":android:firebase",
    ":android:logging",
    ":android:perf",
    ":android:resources",
    ":android:ui:icons",
    ":android:ui:list",
    ":android:ui:strings",
    ":android:ui:themes",

    ":fake:analytics",
    ":fake:perf",
)
