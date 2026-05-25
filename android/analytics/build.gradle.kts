plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.metro)
}

android {
    namespace = "net.sigmabeta.sage.analytics"
}

// Metro applied alongside Hilt during the migration; interop.includeDagger() recognises
// existing @Module / @Provides / @Inject / @Singleton annotations as Metro contributions.
// @ContributesTo(AppScope::class) on this module's @Module declaration flows the bindings
// into whichever Metro graph the downstream consumer declares for AppScope.
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
}
