plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.kmp.js)
}

kotlin {
    androidLibrary {
        namespace = "net.sigmabeta.sage.common.pdf"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(projects.common.images)
            }
        }
    }
}
