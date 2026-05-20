plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.compose.kmp)
}

// ListScreen + GridScreen are pure Compose Multiplatform — LazyColumn /
// LazyVerticalGrid / LazyVerticalStaggeredGrid and WindowInsets.navigationBars all
// resolve from CMP foundation. NavArgType pins to the Android-only AndroidX
// navigation library and stays in androidMain until a multiplatform replacement
// (Voyager — see Milestone 6 slice 6) lands here too.
kotlin {
    androidLibrary {
        namespace = "net.sigmabeta.sage.android.ui.list"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.common.appcomm)
                api(projects.common.list)
                api(projects.common.nav)
                api(projects.common.ui.components)
            }
        }
        named("androidMain") {
            dependencies {
                api(libs.androidx.navigation.compose)
            }
        }
    }
}
