plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.detekt) apply false
    // Provides the ktlint runtime for the base plugins' configureKtlint() (applied by id), same as
    // detekt above — needed when SAGE is built standalone.
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
}

// Per-module Maven `group` is assigned via gradle.lifecycle.beforeProject in settings.gradle.kts
// (configure-on-demand / isolated-projects friendly), not a cross-project subprojects {} block.
