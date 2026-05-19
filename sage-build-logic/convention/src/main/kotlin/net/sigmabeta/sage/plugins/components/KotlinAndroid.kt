package net.sigmabeta.sage.plugins.components

import com.android.build.api.dsl.CommonExtension
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension,
) {
    configureAndroidLibraryDefaults(commonExtension)
    configureKotlin<KotlinAndroidProjectExtension>()
}

/**
 * Android library DSL defaults shared by the Android and KMP conventions: SDK levels, NDK, and
 * Java 17 source/target. The KMP convention reuses this for its `androidTarget()` while driving
 * Kotlin compiler options through the multiplatform extension instead of [configureKotlin].
 */
internal fun Project.configureAndroidLibraryDefaults(
    commonExtension: CommonExtension,
) {
    commonExtension.apply {
        compileSdk = 36
        ndkVersion = "29.0.14206865"

        defaultConfig.minSdk = 26

        compileOptions.apply {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
}

internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    configureKotlin<KotlinJvmProjectExtension>()
}

private inline fun <reified T : KotlinTopLevelExtension> Project.configureKotlin() = configure<T> {
    configureDetekt()

    val warningsAsErrors: String? by project
    when (this) {
        is KotlinAndroidProjectExtension -> compilerOptions
        is KotlinJvmProjectExtension -> compilerOptions
        else -> TODO("Unsupported project extension $this ${T::class}")
    }.apply {
        jvmTarget.set(JvmTarget.JVM_17)
        allWarningsAsErrors.set(warningsAsErrors.toBoolean())
        freeCompilerArgs.add(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        )
    }

    tasks.withType<Test> {
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 3).coerceAtLeast(1)
    }
}

internal fun Project.configureDetekt() {
    with(pluginManager) {
        apply("io.gitlab.arturbosch.detekt")
    }

    extensions.configure<DetektExtension> {
        config.setFrom("${rootDir.absolutePath}/detekt-config.yml")
        baseline = file("detekt-baseline.xml")
    }
}
