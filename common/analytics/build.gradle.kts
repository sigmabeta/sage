plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    androidLibrary {
        namespace = "net.sigmabeta.sage.common.analytics"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.common.appcomm)
            }
        }
    }
}
