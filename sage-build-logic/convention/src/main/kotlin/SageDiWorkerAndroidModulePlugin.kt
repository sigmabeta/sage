import net.sigmabeta.sage.plugins.components.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class SageDiWorkerAndroidModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(plugin = "sage.di.android")
            }

            dependencies {
                add("implementation", libs.findLibrary("androidx-hilt-work").get())
                add("implementation", libs.findLibrary("androidx-work-manager").get())
                "ksp"(libs.findLibrary("androidx-hilt-compiler").get())
            }
        }
    }
}
