plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.kmp.js)
    alias(libs.plugins.sage.compose.kmp)
}

kotlin {
    androidLibrary {
        namespace = "net.sigmabeta.sage.ui.icons"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(projects.common.ui.iconsApi)

                api(libs.jetbrains.compose.material.icons.extended)
            }
        }
    }
}
