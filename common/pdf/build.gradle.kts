plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    androidLibrary {
        namespace = "net.sigmabeta.sage.common.pdf"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                implementation(projects.common.images)
            }
        }
    }
}
