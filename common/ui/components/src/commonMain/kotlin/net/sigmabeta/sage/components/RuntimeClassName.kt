package net.sigmabeta.sage.components

/**
 * The runtime class name of [obj], used only as an opaque list `contentType` / layout-identity
 * key (see [ListModel.layoutId]). JVM/Android keep the historical fully-qualified
 * `javaClass.name`; the (enforcement-only) JS target has no FQN reflection, so it falls back to
 * the simple name — which is unique across every ListModel subtype anyway, so the recycling
 * key stays distinct.
 */
internal expect fun runtimeClassName(obj: Any): String
