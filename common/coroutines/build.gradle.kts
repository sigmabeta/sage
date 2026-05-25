plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    js { nodejs() }

    androidLibrary {
        namespace = "net.sigmabeta.sage.common.coroutines"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.kotlinx.coroutines.core)
            }
        }
    }
}
