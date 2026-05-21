plugins {
    alias(libs.plugins.sage.android)
}

android {
    namespace = "net.sigmabeta.sage.perf.analytics.firebase"
}

dependencies {
    api(projects.common.perf)
    api(platform(libs.firebase.bom))
    api(libs.firebase.performance)
    implementation(projects.android.coroutines)
}
