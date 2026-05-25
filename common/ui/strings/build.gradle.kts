plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    js { nodejs() }

    androidLibrary {
        namespace = "net.sigmabeta.sage.common.ui.strings"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                implementation(projects.common.connectivity)
            }
        }
    }
}
