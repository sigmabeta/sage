plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
}

// Each top-level namespace gets a distinct Maven group so that modules sharing
// the same project.name (e.g. :common:perf and :fake:perf) don't collide during
// Gradle's module-identity unification.
subprojects {
    group = when {
        path.startsWith(":android:") -> "net.sigmabeta.sage.android"
        path.startsWith(":fake:") -> "net.sigmabeta.sage.fake"
        else -> "net.sigmabeta.sage"
    }
}
