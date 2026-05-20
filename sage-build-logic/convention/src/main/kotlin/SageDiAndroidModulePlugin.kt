import dev.zacsweers.metro.gradle.MetroPluginExtension
import net.sigmabeta.sage.plugins.components.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class SageDiAndroidModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
                apply("com.google.dagger.hilt.android")
                apply("dev.zacsweers.metro")
            }

            // Metro is applied alongside Hilt during the migration (see chipbox's
            // docs/metro-migration.md). interop.includeDagger() makes Metro's compiler
            // plugin recognise existing @Inject / @Provides / @Module / @Binds / @Singleton
            // annotations as Dagger-style contributions. After Milestone 6 lands, Hilt and
            // its annotations are removed — this plugin then drops the hilt-android plugin +
            // hilt-compiler KSP step and keeps only Metro.
            extensions.configure<MetroPluginExtension> {
                interop.includeDagger()
            }

            dependencies {
                add("implementation", libs.findLibrary("hilt").get())
                add("implementation", libs.findLibrary("sage.common.di").get())
                "ksp"(libs.findLibrary("hilt.compiler").get())
            }
        }
    }
}
