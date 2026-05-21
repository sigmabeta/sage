import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import net.sigmabeta.sage.plugins.components.chipboxNamespace
import net.sigmabeta.sage.plugins.components.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention for `:features:<x>:real` modules.
 *
 * Layers [SageKmpModulePlugin], [SageComposeKmpModulePlugin], and the Metro DI compiler plugin,
 * and adds the three deps every chipbox feature `:real` reaches for: `sage.common.di` (Metro
 * scope contributions), `metrox.viewmodel` + `metrox.viewmodel.compose` (the
 * `@HiltViewModel`/`hiltViewModel()` replacement after the Hilt → Metro migration).
 *
 * Per-feature `cbox.android.ui.*` / `cbox.common.player.*` deps stay in each module's build
 * file — they differ enough that consolidating them would hide real coupling.
 */
class SageFeatureRealPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("sage.kmp")
                apply("sage.compose.kmp")
                apply("dev.zacsweers.metro")
            }

            val derivedNamespace = chipboxNamespace()
            extensions.configure<KotlinMultiplatformExtension> {
                (this as ExtensionAware).extensions.configure(
                    KotlinMultiplatformAndroidLibraryExtension::class.java,
                ) {
                    namespace = derivedNamespace
                }

                sourceSets.getByName("commonMain").dependencies {
                    implementation(libs.findLibrary("sage.common.di").get())
                    implementation(libs.findLibrary("metrox.viewmodel").get())
                    implementation(libs.findLibrary("metrox.viewmodel.compose").get())
                }
            }
        }
    }
}
