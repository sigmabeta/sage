plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.kmp.js)
}

// First sage submodule library to convert from sage.jvm to sage.kmp. Hatchet's interface and
// the two impls (BasicHatchet / BluntHatchet) are pure Kotlin — just println + a when on Int —
// so the sources live in commonMain directly rather than the legacy `src/main/java` shim that
// sage.kmp maps to jvmSharedMain. Other downstream sage libs that depend on logging (appcomm,
// list, ui/components, ui/strings) follow when their own slices land.
kotlin {
    // A non-JVM target makes commonMain compile against the common stdlib, so `java.*` is a real
    // error here instead of silently allowed (both JVM-family targets would otherwise permit it).
    // logging is the keystone dependency for most consumer modules, so enforcing it first lets the
    // non-JVM target spread up the graph. JS is the cheapest such target (no native toolchain).
    androidLibrary {
        namespace = "net.sigmabeta.sage.common.logging"
    }

    sourceSets {
        named("commonTest") {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
