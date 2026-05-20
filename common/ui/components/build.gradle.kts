plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    androidLibrary {
        namespace = "net.sigmabeta.sage.common.ui.components"
    }

    sourceSets {
        named("jvmSharedMain") {
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
