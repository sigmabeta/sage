import dev.zacsweers.metro.gradle.MetroPluginExtension
import net.sigmabeta.sage.plugins.components.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class SageDiJvmModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
                apply("dev.zacsweers.metro")
            }

            // Metro is applied alongside Hilt-core during the migration (see chipbox's
            // docs/metro-migration.md). interop.includeDagger() makes Metro's compiler
            // plugin recognise existing @Inject / @Provides / @Module / @Binds / @Singleton
            // annotations as Dagger-style contributions. After Milestone 6 lands, Hilt is
            // removed — this plugin then drops the hilt-core dep + hilt-compiler KSP step
            // and keeps only Metro.
            extensions.configure<MetroPluginExtension> {
                interop.includeDagger()
            }

            dependencies {
                add("implementation", libs.findLibrary("hilt-core").get())
                add("implementation", libs.findLibrary("sage.common.di").get())
                "ksp"(libs.findLibrary("hilt.compiler").get())
            }
        }
    }
}
