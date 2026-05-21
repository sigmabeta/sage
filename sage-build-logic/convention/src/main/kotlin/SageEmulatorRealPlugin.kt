import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import net.sigmabeta.sage.plugins.components.chipboxNamespace
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention for `:cbox:android:player:emulators:<emu>:real` modules — the pure-Kotlin JNI
 * wrapper that builds for both the Android and JVM variants. The native `.so` is produced
 * outside this module: Android packages it via the sibling `:native` companion
 * (externalNativeBuild can't live in an AGP KMP library), and the JVM target host-builds it.
 *
 * Every emulator `:real` is byte-identical except for the namespace, so the plugin contributes
 * the namespace, the three `jvmSharedMain` deps every wrapper needs, and a `runtimeOnly` dep
 * on the sibling `:native` companion (androidMain only — that's what packages the .so into
 * the APK; the JVM target host-builds its own .so).
 */
class SageEmulatorRealPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("sage.kmp")

            val parentPath = checkNotNull(project.parent) {
                "sage.emulator.real must be applied to a `:<emu>:real` module under a parent emulator project"
            }.path

            val derivedNamespace = chipboxNamespace()
            extensions.configure<KotlinMultiplatformExtension> {
                (this as ExtensionAware).extensions.configure(
                    KotlinMultiplatformAndroidLibraryExtension::class.java,
                ) {
                    namespace = derivedNamespace
                }

                sourceSets.getByName("jvmSharedMain").dependencies {
                    api(project(":cbox:common:player:common:api"))
                    api(project(":cbox:common:player:emulators:api"))
                    implementation(project(":cbox:common:repository:api"))
                }

                sourceSets.getByName("androidMain").dependencies {
                    runtimeOnly(project("$parentPath:native"))
                }
            }
        }
    }
}
