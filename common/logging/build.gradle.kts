plugins {
    alias(libs.plugins.sage.kmp)
}

// First sage submodule library to convert from sage.jvm to sage.kmp. Hatchet's interface and
// the two impls (BasicHatchet / BluntHatchet) are pure Kotlin — just println + a when on Int —
// so the sources live in commonMain directly rather than the legacy `src/main/java` shim that
// sage.kmp maps to jvmSharedMain. Other downstream sage libs that depend on logging (appcomm,
// list, ui/components, ui/strings) follow when their own slices land.
kotlin {
    androidLibrary {
        namespace = "net.sigmabeta.sage.common.logging"
    }
}
