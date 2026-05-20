plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    androidLibrary {
        namespace = "net.sigmabeta.sage.common.coroutines"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(libs.kotlinx.coroutines.core)
            }
        }
    }
}
