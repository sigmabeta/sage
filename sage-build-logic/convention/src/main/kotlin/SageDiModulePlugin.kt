import net.sigmabeta.sage.plugins.components.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Metro-native DI mixin — apply alongside a base module plugin (sage.android / sage.jvm / sage.kmp).
 * Applies the Metro compiler plugin and contributes to AppScope (sage/common/di) via
 * `@ContributesTo` / `@ContributesBinding` / `@ContributesIntoMap`. Hilt + plain Dagger were dropped
 * at chipbox Milestone 6 (see chipbox docs/metro-migration.md); annotations are all Metro-native.
 *
 * Replaces the former platform-split `sage.di.android` / `sage.di.jvm` plugins, which had become
 * byte-identical once the Hilt/Dagger era ended (the split only mattered while Android used Hilt and
 * the JVM used plain Dagger).
 */
class SageDiModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("dev.zacsweers.metro")

            dependencies {
                add("implementation", libs.findLibrary("sage.common.di").get())
            }
        }
    }
}
