plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidLibrary {
        namespace = "net.sigmabeta.sage.common.appcomm"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                implementation(libs.kotlinx.serialization.core)
                implementation(projects.common.logging)
            }
        }
    }
}
