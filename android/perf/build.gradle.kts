plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.compose.kmp)
}

kotlin {
    androidLibrary {
        namespace = "net.sigmabeta.sage.perf"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.common.logging)
            }
        }
    }
}
