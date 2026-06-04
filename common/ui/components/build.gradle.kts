plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.kmp.js)
}

kotlin {
    android {
        namespace = "net.sigmabeta.sage.common.ui.components"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.kotlinx.collections.immutable)
                api(projects.common.appcomm)
                api(projects.common.images)
                api(projects.common.pdf)
                api(projects.common.ui.iconsApi)
            }
        }
    }
}
