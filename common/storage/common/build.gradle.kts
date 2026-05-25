plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    js { nodejs() }

    androidLibrary {
        namespace = "net.sigmabeta.sage.common.storage.common"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.common.coroutines)
            }
        }
    }
}
