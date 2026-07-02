import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Opt-in Kotlin/JS (Node) target for `sage.kmp` modules that participate in the web build
 * (`apps/js`). Apply it alongside `sage.kmp` — directly (`alias(libs.plugins.sage.kmp.js)`) or
 * transitively (the chipbox `feature.api`/`feature.real` plugins apply it for every feature).
 *
 * Why a plugin instead of a per-module `js { nodejs() }`: it lets the JS target be **gated**.
 * The Kotlin/JS root plugins (NodeJs/Yarn, applied to the root project the first time any module
 * configures a JS target) are incompatible with `org.gradle.configureondemand=true` and spam a
 * warning on every build. This plugin only configures the target when the `sage.js` Gradle
 * property is set, so ordinary Android/JVM builds never touch Kotlin/JS and stay quiet. Web
 * builds pass `-Psage.js=true`; `apps/js` requires it, since it consumes the JS variants these
 * modules then expose.
 *
 * `nodejs()` here only selects the *library* test/tooling host — the produced klib is
 * environment-agnostic. `apps/js` declares its own `browser()` executable target; it does not
 * apply `sage.kmp` and is unaffected by the gate.
 *
 * Modules deliberately kept off the web build (JVM/Android-only impls) simply don't apply this.
 */
class SageKmpJsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val jsEnabled = providers.gradleProperty("sage.js").orNull.toBoolean()
            if (!jsEnabled) return

            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                extensions.configure<KotlinMultiplatformExtension> {
                    js { nodejs() }
                }
            }
        }
    }
}
