plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.metro)
}

android {
    namespace = "net.sigmabeta.sage.analytics"
}

// Metro applied alongside Hilt during the migration; interop.includeDagger() recognises
// existing @Module / @Provides / @Inject / @Singleton annotations as Metro contributions.
// @ContributesTo(AppScope::class) on this module's @Module declaration flows the bindings
// into whichever Metro graph declares AppScope (downstream: chipbox's ChipboxAppGraph).
metro {
    interop {
        includeDagger()
    }
}

dependencies {
    api(projects.common.analytics)
    implementation(projects.common.di)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(projects.android.coroutines)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}
