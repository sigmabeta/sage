plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.kmp.js)
}

kotlin {
    android {
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
