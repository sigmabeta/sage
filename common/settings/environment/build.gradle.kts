plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "net.sigmabeta.sage.common.settings.environment"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.common.coroutines)
                api(projects.common.storage.common)
            }
        }
    }
}
