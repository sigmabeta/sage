package net.sigmabeta.sage.plugins.components

import org.gradle.api.Project

/**
 * Derives a module's `namespace` from its Gradle path using the chipbox-wide convention.
 *
 * Rule:
 *  - Drop the leading colon.
 *  - Drop the `cbox` segment (KMP migration collapsed the old `cbox/jvm` + `cbox/android` twins
 *    onto single modules; the layer is implied by the rest of the path).
 *  - Drop the `android` segment immediately after `cbox` (post-KMP, an `:android` subpath is just
 *    where the module lives, not a separate namespace layer).
 *  - Strip hyphens from each segment (`browse-all-tracks` → `browsealltracks`); package identifiers
 *    can't contain them.
 *  - Map `native` → `nativelib` (Java keyword).
 *  - Prepend `net.sigmabeta.chipbox.`.
 *
 * Examples:
 *  - `:cbox:common:player:buffer:api` → `net.sigmabeta.chipbox.common.player.buffer.api`
 *  - `:cbox:android:player:emulators:gba:real` → `net.sigmabeta.chipbox.player.emulators.gba.real`
 *  - `:cbox:android:player:emulators:gba:native` → `net.sigmabeta.chipbox.player.emulators.gba.nativelib`
 *  - `:features:browse-all-tracks:api` → `net.sigmabeta.chipbox.features.browsealltracks.api`
 *
 * Modules whose source layout predates the rule (e.g. `cbox/android/colors/api` with sources under
 * `chipbox/colors/`) keep an explicit `namespace = "..."` override in their build file; moving
 * those source trees is out of scope for the convention-plugin sweep.
 */
fun Project.chipboxNamespace(): String {
    val segments = path.removePrefix(":").split(":")
    val cleaned = segments
        .filterNot { it == "cbox" || it == "android" }
        .map { it.replace("-", "") }
        .map { if (it == "native") "nativelib" else it }
    return "net.sigmabeta.chipbox." + cleaned.joinToString(".")
}
