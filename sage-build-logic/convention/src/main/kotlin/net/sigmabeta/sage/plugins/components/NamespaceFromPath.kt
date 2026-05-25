package net.sigmabeta.sage.plugins.components

import org.gradle.api.Project

/**
 * Derives a module's `namespace` from its Gradle path, app-agnostically. The package prefix and any
 * structural path segments to drop are project-specific, so they're supplied via Gradle properties
 * (typically set once in the consuming app's root `gradle.properties`):
 *
 *  - `sage.namespace.prefix` (required) — the package root, e.g. `com.example.myapp`.
 *  - `sage.namespace.dropSegments` (optional, comma-separated) — path segments that are structural
 *    rather than part of the package, e.g. `core,platform`.
 *
 * Each remaining segment has hyphens stripped (`browse-all-tracks` → `browsealltracks`; package
 * identifiers can't contain them) and `native` is mapped to `nativelib` (a Java keyword).
 *
 * With `sage.namespace.prefix=com.example.app` and `sage.namespace.dropSegments=core,platform`:
 *  - `:core:data:db:api` → `com.example.app.data.db.api`
 *  - `:core:platform:audio:real` → `com.example.app.audio.real`
 *  - `:core:platform:audio:native` → `com.example.app.audio.nativelib`
 *  - `:features:now-playing:api` → `com.example.app.features.nowplaying.api`
 */
fun Project.namespaceFromPath(): String {
    val prefix = findProperty("sage.namespace.prefix") as String?
        ?: error("namespaceFromPath() needs `sage.namespace.prefix` set in gradle.properties.")
    val dropSegments = (findProperty("sage.namespace.dropSegments") as String?)
        ?.split(",")
        ?.map { it.trim() }
        ?.filter { it.isNotEmpty() }
        ?.toSet()
        .orEmpty()

    val cleaned = path.removePrefix(":").split(":")
        .filterNot { it in dropSegments }
        .map { it.replace("-", "") }
        .map { if (it == "native") "nativelib" else it }
    return "$prefix." + cleaned.joinToString(".")
}
