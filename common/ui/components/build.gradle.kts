plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    js { nodejs() }

    androidLibrary {
        namespace = "net.sigmabeta.sage.common.ui.components"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.kotlinx.collections.immutable)
                api(projects.common.appcomm)
                api(projects.common.images)
                api(projects.common.pdf)
                api(projects.common.ui.icons)
            }
        }
    }
}
