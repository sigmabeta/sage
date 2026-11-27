plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.kmp.js)
    alias(libs.plugins.sage.compose.kmp)
}

kotlin {
    android {
        namespace = "net.sigmabeta.sage.ui.componentlibrary"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                // Re-exported: the catalog returns `ListModel`s, the config uses `ColumnType`,
                // actions are `SageAction`s, and the host reads the renderer suite's locals.
                api(projects.common.ui.components)
                api(projects.common.list)
                api(projects.common.appcomm)
                api(projects.common.ui.composables)

                implementation(libs.kotlinx.collections.immutable)
            }
        }

        named("jvmSharedMain") {
            dependencies {
                // StringGenerator, for the seeded sample text.
                implementation(projects.common.ui.strings)
            }
        }

        named("jvmTest") {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
