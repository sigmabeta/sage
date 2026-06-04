plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "net.sigmabeta.sage.common.debug"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.common.coroutines)
                implementation(projects.common.settings.general)
            }
        }
    }
}
