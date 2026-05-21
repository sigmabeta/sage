import com.android.build.api.dsl.LibraryExtension
import net.sigmabeta.sage.plugins.components.chipboxNamespace
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * Convention for `:cbox:android:player:emulators:<emu>:di` modules. Pulls the sibling
 * `:real` and `:native` companions into the Android app classpath so the emulator's Metro
 * bindings and packaged `.so` reach `:apps:android`.
 *
 * The `:native` runtime-only dep is what packages the `.so` into the APK — a KMP `:real`
 * can't host the CMake trigger because AGP 9's KMP library DSL has no `externalNativeBuild`.
 */
class SageEmulatorDiPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("sage.android")
                apply("sage.di.android")
            }

            extensions.configure<LibraryExtension> {
                namespace = chipboxNamespace()
            }

            val parentPath = checkNotNull(project.parent) {
                "sage.emulator.di must be applied to a `:<emu>:di` module under a parent emulator project"
            }.path

            dependencies {
                add("api", project("$parentPath:real"))
                add("runtimeOnly", project("$parentPath:native"))
            }
        }
    }
}
