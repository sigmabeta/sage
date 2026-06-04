import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import net.sigmabeta.sage.plugins.components.configureDetekt
import net.sigmabeta.sage.plugins.components.configureUniqueArchiveBaseName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

/**
 * Kotlin Multiplatform convention for shared core modules that must run in both the Android app
 * and the JVM target. Mirrors [SageJvmModulePlugin]/[SageAndroidModulePlugin] (JVM 17, detekt,
 * warnings-as-errors, coroutines opt-in) but produces a single multiplatform artifact with a
 * `jvm()` target and an Android library target (AGP 9's `com.android.kotlin.multiplatform.library`
 * — `com.android.library` is no longer compatible with the KMP plugin).
 *
 * Both targets are JVM-family, so `commonMain` (Kotlin common stdlib only — no `java.*`) is not
 * where most existing code can live. A `jvmSharedMain`/`jvmSharedTest` intermediate source set
 * sits between `commonMain` and the two platform source sets and is mapped to the legacy
 * `src/main/java` / `src/test/java` directories, so a module can adopt this plugin with no file
 * moves and no code changes; `java.*` stays available there. Pure code can be hoisted up into
 * `commonMain` incrementally later. Per-module `namespace` is set in the module build file via
 * `kotlin { android { namespace = "..." } }`.
 */
class SageKmpModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.kotlin.multiplatform.library")
                apply("io.gitlab.arturbosch.detekt")
            }

            val warningsAsErrors: String? by project

            extensions.configure<KotlinMultiplatformExtension> {
                jvm()

                (this as ExtensionAware).extensions.configure(
                    KotlinMultiplatformAndroidLibraryExtension::class.java,
                ) {
                    compileSdk = 36
                    minSdk = 26
                    // Enable the JVM-host (not on-device) test component for the Android target.
                    // Every shared module has a commonTest source set; without this AGP can't run
                    // those tests against the Android target and warns once per module at configure
                    // time. It also activates the `androidHostTest` source set the dependsOn wiring
                    // below targets. CI is unaffected — it invokes `jvmTest` explicitly, not `check`.
                    withHostTest { }
                }

                compilerOptions {
                    allWarningsAsErrors.set(warningsAsErrors.toBoolean())
                    freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
                }

                sourceSets.apply {
                    val jvmSharedMain = maybeCreate("jvmSharedMain").apply {
                        dependsOn(getByName("commonMain"))
                        kotlin.srcDir("src/main/java")
                        resources.srcDir("src/main/resources")
                    }
                    val jvmSharedTest = maybeCreate("jvmSharedTest").apply {
                        dependsOn(getByName("commonTest"))
                        kotlin.srcDir("src/test/java")
                        resources.srcDir("src/test/resources")
                    }

                    getByName("jvmMain").dependsOn(jvmSharedMain)
                    getByName("jvmTest").dependsOn(jvmSharedTest)
                    getByName("androidMain").dependsOn(jvmSharedMain)
                    findByName("androidHostTest")?.dependsOn(jvmSharedTest)
                }
            }

            configureDetekt()
            configureUniqueArchiveBaseName()

            tasks.withType<KotlinJvmCompile>().configureEach {
                compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
            }

            tasks.withType<Test>().configureEach {
                maxParallelForks = (Runtime.getRuntime().availableProcessors() / 3).coerceAtLeast(1)
            }
        }
    }
}
