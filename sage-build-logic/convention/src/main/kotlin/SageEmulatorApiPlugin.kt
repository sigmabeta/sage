import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import net.sigmabeta.sage.plugins.components.chipboxNamespace
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention for `:cbox:android:player:emulators:<emu>:api` modules. Every emulator's `:api`
 * module is byte-identical except for the namespace; this plugin contributes the namespace via
 * [chipboxNamespace] and the [SageKmpModulePlugin] base, leaving consumer build files at one
 * `plugins { alias(libs.plugins.sage.emulator.api) }` line.
 */
class SageEmulatorApiPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("sage.kmp")

            val derivedNamespace = chipboxNamespace()
            extensions.configure<KotlinMultiplatformExtension> {
                (this as ExtensionAware).extensions.configure(
                    KotlinMultiplatformAndroidLibraryExtension::class.java,
                ) {
                    namespace = derivedNamespace
                }
            }
        }
    }
}
