plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.compose.kmp)
}

kotlin {
    js { nodejs() }

    androidLibrary {
        namespace = "net.sigmabeta.sage.ui.perf"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.common.logging)
            }
        }
    }
}
