plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "net.sigmabeta.sage.common.time"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.kotlinx.datetime)
            }
        }
    }
}
