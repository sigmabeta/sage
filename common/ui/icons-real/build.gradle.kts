plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.compose.kmp)
}

kotlin {
    androidLibrary {
        namespace = "net.sigmabeta.sage.ui.icons"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(projects.common.ui.icons)

                api(libs.jetbrains.compose.material.icons.extended)
            }
        }
    }
}
