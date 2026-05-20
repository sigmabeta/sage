plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    androidLibrary {
        namespace = "net.sigmabeta.sage.common.freeform"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.common.appcomm)
                api(projects.common.ui.strings)
                api(projects.common.ui.components)
            }
        }
    }
}
