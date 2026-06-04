plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "net.sigmabeta.sage.common.events"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.common.analytics)
                api(projects.common.appcomm)
            }
        }
    }
}
