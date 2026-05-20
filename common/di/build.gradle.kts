plugins {
    alias(libs.plugins.sage.kmp)
}

// Marker types for Metro dependency graphs. Pure-Kotlin classes — no Metro runtime dep here;
// consumers apply the Metro plugin and reference these by FQCN in their @DependencyGraph /
// @ContributesTo / @SingleIn / @ContributesBinding annotations. Hosted in sage so any module
// (sage or downstream) can contribute to the same scope without a sage → app cycle.
kotlin {
    androidLibrary {
        namespace = "net.sigmabeta.sage.common.di"
    }
}
