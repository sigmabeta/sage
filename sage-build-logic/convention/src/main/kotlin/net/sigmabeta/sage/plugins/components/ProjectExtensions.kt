package net.sigmabeta.sage.plugins.components

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

/**
 * Derive each module's Jar `archiveBaseName` from its Gradle path so that flat distributions
 * (the `application` plugin's `installDist` / `distZip`) don't collide on `api.jar` / `real.jar`.
 * `:foo:bar:api` becomes `foo-bar-api.jar`; KMP target suffixes (`-jvm`, `-androidJvm`) still tack
 * on as usual, so KMP modules end up at e.g. `foo-bar-api-jvm.jar`.
 *
 * Applies to every `Jar` task — `jar`, `sourcesJar`, KMP per-target jars — so any future task
 * that emits a jar inherits the unique name without further wiring.
 */
internal fun Project.configureUniqueArchiveBaseName() {
    val slug = path.removePrefix(":").replace(':', '-')
    tasks.withType<Jar>().configureEach {
        archiveBaseName.set(slug)
    }
}
