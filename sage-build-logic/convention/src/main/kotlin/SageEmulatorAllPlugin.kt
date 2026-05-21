import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import net.sigmabeta.sage.plugins.components.chipboxNamespace
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention for `:cbox:android:player:emulators:<emu>:all` modules — the aggregating module
 * that re-exposes the emulator's `:api` + `:real` as a single dependency for consumers that
 * don't need the DI wiring.
 */
class SageEmulatorAllPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("sage.kmp")

            val parentPath = checkNotNull(project.parent) {
                "sage.emulator.all must be applied to a `:<emu>:all` module under a parent emulator project"
            }.path

            val derivedNamespace = chipboxNamespace()
            extensions.configure<KotlinMultiplatformExtension> {
                (this as ExtensionAware).extensions.configure(
                    KotlinMultiplatformAndroidLibraryExtension::class.java,
                ) {
                    namespace = derivedNamespace
                }

                sourceSets.getByName("jvmSharedMain").dependencies {
                    api(project("$parentPath:api"))
                    api(project("$parentPath:real"))
                }
            }
        }
    }
}
