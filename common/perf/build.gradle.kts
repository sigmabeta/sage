plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "net.sigmabeta.sage.common.perf"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.common.coroutines)
                api(projects.common.analytics)
            }
        }
    }
}
