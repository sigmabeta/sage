plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    js { nodejs() }

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
