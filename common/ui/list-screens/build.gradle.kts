plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.kmp.js)
    alias(libs.plugins.sage.compose.kmp)
}

// ListScreen + GridScreen are pure Compose Multiplatform — LazyColumn /
// LazyVerticalGrid / LazyVerticalStaggeredGrid and WindowInsets.navigationBars all
// resolve from CMP foundation. (The old NavArgType androidMain helper that pinned to
// AndroidX navigation was dropped after the Voyager migration — the module is now
// fully multiplatform with no androidMain code.)
//
// ReorderableScreen adds one external dep — sh.calvin.reorderable — for drag-and-drop
// reorder; it ships every target this module builds for (android/jvm/js/wasm).
kotlin {
    android {
        namespace = "net.sigmabeta.sage.ui.list"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.common.appcomm)
                api(projects.common.list)
                api(projects.common.nav)
                api(projects.common.ui.components)

                // Drag-and-drop reorder for ReorderableScreen (see catalog comment).
                implementation(libs.reorderable)
            }
        }
    }
}
