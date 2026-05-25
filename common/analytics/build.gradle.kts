plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    js { nodejs() }

    androidLibrary {
        namespace = "net.sigmabeta.sage.common.analytics"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.common.appcomm)
            }
        }
    }
}
