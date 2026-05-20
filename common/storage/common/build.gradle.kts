plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    androidLibrary {
        namespace = "net.sigmabeta.sage.common.storage.common"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.common.coroutines)
            }
        }
    }
}
