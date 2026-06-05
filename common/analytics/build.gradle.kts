plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.kmp.js)
}

kotlin {
    android {
        namespace = "net.sigmabeta.sage.common.analytics"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.common.appcomm)
                // NoopAnalytics logs through Hatchet.
                implementation(projects.common.logging)
            }
        }
    }
}
