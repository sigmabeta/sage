plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.kmp.js)
}

kotlin {
    android {
        namespace = "net.sigmabeta.sage.common.freeform"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.common.appcomm)
                api(projects.common.ui.strings)
                api(projects.common.ui.components)
            }
        }
    }
}
