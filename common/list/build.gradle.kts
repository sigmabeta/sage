plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.kmp.js)
}

kotlin {
    androidLibrary {
        namespace = "net.sigmabeta.sage.common.list"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.kotlinx.collections.immutable)
                api(projects.common.appcomm)
                api(projects.common.analytics)
                api(projects.common.coroutines)
                api(projects.common.logging)
                api(projects.common.nav)
                api(projects.common.ui.strings)
                implementation(projects.common.ui.components)
            }
        }
    }
}
