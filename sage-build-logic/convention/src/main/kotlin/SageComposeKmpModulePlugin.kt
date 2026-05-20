import net.sigmabeta.sage.plugins.components.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Compose Multiplatform layer over [SageKmpModulePlugin]. Apply both:
 *
 *   plugins {
 *       alias(libs.plugins.sage.kmp)
 *       alias(libs.plugins.sage.compose.kmp)
 *   }
 *
 * Applies the Kotlin Compose compiler plugin (the same `org.jetbrains.kotlin.plugin.compose`
 * that [SageComposeAndroidModulePlugin] applies on the Android side) and adds the JetBrains
 * Compose runtime / foundation / material3 / ui libs to `commonMain`. Android and JVM both
 * inherit transparently — Android via Skia-on-device, JVM via the AWT/Skia desktop backend.
 *
 * The `org.jetbrains.compose` Gradle plugin is intentionally NOT applied here; it ships a
 * packaging DSL (`compose.desktop { ... }`) only application projects need, and would also
 * override deps we want pinned via the catalog. App targets that need the per-OS Skia native
 * (e.g. `apps/jvm`) apply that plugin themselves and reference `compose.desktop.currentOs`.
 *
 * Stability-config / reports wiring duplicates the body of `configureKotlinCompose()` in
 * `components/AndroidCompose.kt`; that helper is `private` to the Android path today.
 * Extracting it for reuse is a fine follow-up if a third Compose-flavoured convention plugin
 * ever shows up.
 */
class SageComposeKmpModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            extensions.configure<ComposeCompilerGradlePluginExtension> {
                val rootFile = rootProject.layout.projectDirectory.file("compose-stability.conf")
                if (rootFile.asFile.exists()) {
                    stabilityConfigurationFiles.add(rootFile)
                }

                val sageDir = gradle.includedBuilds.firstOrNull { it.name == "sage" }?.projectDir
                if (sageDir != null) {
                    val sageConfigFile = sageDir.resolve("compose-stability.conf")
                    if (sageConfigFile.exists()) {
                        stabilityConfigurationFiles.add(
                            layout.file(provider { sageConfigFile })
                        )
                    }
                }

                reportsDestination.set(layout.buildDirectory.dir("compose_compiler/reports"))
                metricsDestination.set(layout.buildDirectory.dir("compose_compiler/metrics"))
            }

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.getByName("commonMain").dependencies {
                    implementation(libs.findLibrary("jetbrains-compose-runtime").get())
                    implementation(libs.findLibrary("jetbrains-compose-foundation").get())
                    implementation(libs.findLibrary("jetbrains-compose-material3").get())
                    implementation(libs.findLibrary("jetbrains-compose-ui").get())
                }
            }
        }
    }
}
