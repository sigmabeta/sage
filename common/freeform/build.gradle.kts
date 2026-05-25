plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    js { nodejs() }

    androidLibrary {
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
