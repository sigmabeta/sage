package net.sigmabeta.sage.plugins.components

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension,
) {
    commonExtension.apply {
        buildFeatures.compose = true

        configureKotlinCompose()

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()

            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))

            add("implementation", libs.findLibrary("androidx-compose-material3").get())
            add("implementation", libs.findLibrary("androidx-compose-foundation").get())
            add("implementation", libs.findLibrary("androidx-compose-runtime-tracing").get())
            add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())

            add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
        }
    }
}

private fun Project.configureKotlinCompose() {
    extensions.configure<ComposeCompilerGradlePluginExtension> {
        // App-level overrides: <rootProject>/compose-stability.conf, if present.
        val rootFile = rootProject.layout.projectDirectory.file("compose-stability.conf")
        if (rootFile.asFile.exists()) {
            stabilityConfigurationFiles.add(rootFile)
        }

        // SAGE-shipped baseline: when SAGE is an included build (e.g. consumed
        // by a downstream app), also read its compose-stability.conf so SAGE
        // type rules don't have to be duplicated in every consumer.
        val sageDir = gradle.includedBuilds.firstOrNull { it.name == "sage" }?.projectDir
        if (sageDir != null) {
            val sageConfigFile = sageDir.resolve("compose-stability.conf")
            if (sageConfigFile.exists()) {
                stabilityConfigurationFiles.add(
                    project.layout.file(project.provider { sageConfigFile })
                )
            }
        }

        reportsDestination.set(project.layout.buildDirectory.dir("compose_compiler/reports"))
        metricsDestination.set(project.layout.buildDirectory.dir("compose_compiler/metrics"))
    }

    tasks.withType<Test> {
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 3).coerceAtLeast(1)
    }
}
