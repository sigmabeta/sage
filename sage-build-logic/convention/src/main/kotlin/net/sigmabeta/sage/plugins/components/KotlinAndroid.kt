package net.sigmabeta.sage.plugins.components

import com.android.build.api.dsl.CommonExtension
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.tasks.BaseKtLintCheckTask
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
    configureKtlint()

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

    // Compose-resource / KSP codegen registers generated sources (Res.kt, *Strings*.kt,
    // *_Impl.kt) into the Kotlin source sets with absolute paths; exclude anything under a build
    // dir so detekt only sees hand-written code. A source-root-relative glob wouldn't match the
    // absolute-path entries, hence the path predicate.
    tasks.withType<Detekt>().configureEach {
        exclude { it.file.path.contains("/build/") }
    }
}

/**
 * ktlint, mirroring [configureDetekt]: applies the plugin (runtime provided by the consuming
 * build's root via `alias(libs.plugins.ktlint) apply false`), pins the engine to the catalog's
 * `ktlintTool` version so the ruleset doesn't drift, and excludes generated sources under build
 * dirs. Applied per-module by the base plugins, so ktlint stays co-resident with each module's
 * Kotlin plugin — which ktlint-gradle's multiplatform integration requires.
 */
internal fun Project.configureKtlint() {
    with(pluginManager) {
        apply("org.jlleitschuh.gradle.ktlint")
    }

    val ktlintToolVersion = extensions.getByType<VersionCatalogsExtension>()
        .named("libs")
        .findVersion("ktlintTool")
        .get()
        .requiredVersion

    extensions.configure<KtlintExtension> {
        version.set(ktlintToolVersion)
    }

    tasks.withType<BaseKtLintCheckTask>().configureEach {
        exclude { it.file.path.contains("/build/") }
    }
}
