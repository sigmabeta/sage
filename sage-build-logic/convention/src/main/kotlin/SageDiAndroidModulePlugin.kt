import net.sigmabeta.sage.plugins.components.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class SageDiAndroidModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("dev.zacsweers.metro")

            // Metro-native DI for chipbox android-flavored modules: contributes to AppScope
            // (sage/common/di) via @ContributesTo / @ContributesBinding / @ContributesIntoMap.
            // Hilt + plain Dagger were dropped at chipbox Milestone 6 (see chipbox
            // docs/metro-migration.md); annotations are all Metro-native (no Dagger interop).
            dependencies {
                add("implementation", libs.findLibrary("sage.common.di").get())
            }
        }
    }
}
