plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.kmp.js)
    alias(libs.plugins.sage.compose.kmp)
}

kotlin {
    android {
        namespace = "net.sigmabeta.sage.ui.composables"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                // Re-exported so consumers get SAGE's ListModel vocabulary, the string/action seams
                // the renderers' public signatures name, and the `ArtworkCorner` image type.
                api(projects.common.images)
                api(projects.common.ui.components)
                api(projects.common.ui.strings)
                api(projects.common.appcomm)

                implementation(projects.common.ui.iconsReal)
                implementation(projects.common.ui.perfCompose)

                // `CrossfadeImage`'s Coil request/painter plumbing and the memory-cache handoff.
                implementation(libs.coil.kt.core)
                implementation(libs.coil.kt.compose)
                implementation(libs.coil.kt.meta)
                // `platformImageModel` hands Coil an okio Path for raw filesystem paths.
                implementation(libs.okio)
                // SectionListItem's placeholder phase reads an ImmutableList.
                implementation(libs.kotlinx.collections.immutable)
            }
        }
    }
}

dependencies {
    // The Android actual of `TraceSection`: Perfetto / systrace sections the device scroll
    // macrobenchmark sums. Android-only; the JVM and JS actuals are no-ops.
    add("androidMainImplementation", libs.androidx.tracing.ktx)

    // @Preview harnesses for the component renderers. Declared through the top-level handler so
    // the AndroidX BOM uses Gradle's `platform()`, not the KMP source-set DSL's deprecated one.
    add("androidMainImplementation", platform(libs.androidx.compose.bom))
    add("androidMainImplementation", libs.androidx.compose.ui.tooling.preview)
    add("androidMainImplementation", libs.androidx.compose.ui.tooling)
}
