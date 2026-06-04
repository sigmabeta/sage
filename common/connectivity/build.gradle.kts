plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "net.sigmabeta.sage.common.connectivity"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                implementation(projects.common.coroutines)
            }
        }
    }
}
