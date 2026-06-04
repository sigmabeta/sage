plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.kmp.js)
}

kotlin {
    android {
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
