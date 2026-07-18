package net.sigmabeta.sage.plugins.components

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

/**
 * Shared Kotlin Compose-compiler configuration, used by both the Android
 * ([configureAndroidCompose]) and Multiplatform (SageComposeKmpModulePlugin) convention plugins:
 *
 *  - App-level overrides: `<rootProject>/compose-stability.conf`, if present.
 *  - SAGE-shipped baseline: when SAGE is an included build (consumed by a downstream app), also
 *    read its `compose-stability.conf` so SAGE type rules don't have to be duplicated per consumer.
 *  - Point the compiler reports/metrics at the module build dir.
 */
internal fun Project.configureComposeCompiler() {
    extensions.configure<ComposeCompilerGradlePluginExtension> {
        // `isolated.rootProject` (not `rootProject.layout`) reads the root directory in an
        // Isolated-Projects-safe way — a plain `rootProject.layout` access is a project reaching
        // into another project's state, which IP forbids.
        val rootFile = isolated.rootProject.projectDirectory.file("compose-stability.conf")
        if (rootFile.asFile.exists()) {
            stabilityConfigurationFiles.add(rootFile)
        }

        val sageDir = gradle.includedBuilds.firstOrNull { it.name == "sage" }?.projectDir
        if (sageDir != null) {
            val sageConfigFile = sageDir.resolve("compose-stability.conf")
            if (sageConfigFile.exists()) {
                stabilityConfigurationFiles.add(
                    layout.file(provider { sageConfigFile }),
                )
            }
        }

        reportsDestination.set(layout.buildDirectory.dir("compose_compiler/reports"))
        metricsDestination.set(layout.buildDirectory.dir("compose_compiler/metrics"))
    }
}
