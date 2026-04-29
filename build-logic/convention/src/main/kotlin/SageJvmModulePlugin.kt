import net.sigmabeta.sage.plugins.components.configureKotlinJvm
import net.sigmabeta.sage.plugins.components.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class SageJvmModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("io.gitlab.arturbosch.detekt")
            }

            configureKotlinJvm()

            dependencies {
                add("implementation", libs.findLibrary("kotlin.stdlib").get())
            }
        }
    }
}
