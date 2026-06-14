import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import net.sigmabeta.sage.plugins.components.configureComposeCompiler
import net.sigmabeta.sage.plugins.components.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for a UI-test module that drives the real Compose UI via `runComposeUiTest`
 * on TWO targets from one set of specs: the JVM desktop host (`jvmTest`) and a real Android device
 * (`androidDeviceTest`, instrumented). Apply alongside `sage.kmp`:
 *
 *     plugins {
 *         alias(libs.plugins.sage.kmp)
 *         alias(libs.plugins.sage.compose.uitest)
 *     }
 *
 * What it wires:
 *  - The Compose compiler + JetBrains Compose Gradle plugins (the latter is what exposes
 *    `compose.uiTest` / `compose.desktop.currentOs`).
 *  - The on-device test component (`withDeviceTest`), so the Android half runs instrumented — where
 *    `runComposeUiTest` has a real framework — rather than `androidHostTest` (which lacks one) or
 *    Robolectric.
 *  - `src/uiTest/kotlin` into BOTH `jvmTest` and `androidDeviceTest`, with the compose-test and
 *    instrumentation deps each tree needs. The dir is kept off `androidHostTest` on purpose
 *    (no Android framework there → runComposeUiTest NPEs). The two trees can't `dependsOn` each
 *    other (KMP forbids it across the unit-test / instrumented-test trees), so each carries the deps.
 *
 * The app-specific harness deps (the DI graph's feature modules + fakes) stay in the module build.
 */
@OptIn(ExperimentalComposeLibrary::class)
class SageComposeUiTestModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }

            configureComposeCompiler()

            // The compose dependency notations as plugin code sees them (the build-script `compose`
            // accessor isn't available here). Pinned to the Compose Gradle plugin's own version, so
            // it matches what `compose.desktop.currentOs` resolves.
            val compose = ComposePlugin.Dependencies(this)

            extensions.configure<KotlinMultiplatformExtension> {
                (this as ExtensionAware).extensions.configure(
                    KotlinMultiplatformAndroidLibraryExtension::class.java,
                ) {
                    withDeviceTest {
                        instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    }
                }

                sourceSets.apply {
                    // runComposeUiTest + Compose UI, multiplatform. In commonTest so jvmTest (the
                    // unit-test tree) inherits it; the instrumented tree re-declares below.
                    getByName("commonTest").dependencies {
                        implementation(compose.uiTest)
                        implementation(compose.material3)
                    }

                    getByName("jvmTest").apply {
                        kotlin.srcDir("src/uiTest/kotlin")
                        dependencies {
                            // Per-OS Skia native — the desktop backend renders onto.
                            implementation(compose.desktop.currentOs)
                        }
                    }

                    getByName("androidDeviceTest").apply {
                        kotlin.srcDir("src/uiTest/kotlin")
                        dependencies {
                            implementation(compose.uiTest)
                            implementation(compose.material3)
                            implementation(kotlin("test"))
                            // The empty host Activity the on-device Compose test renders into.
                            implementation(libs.findLibrary("androidx-compose-ui-testing-manifest").get())
                            // AndroidJUnitRunner + the instrumentation registry the device tests use.
                            implementation(libs.findLibrary("androidx-test-runner").get())
                            implementation(libs.findLibrary("androidx-test-ext-junit").get())
                        }
                    }
                }
            }
        }
    }
}
