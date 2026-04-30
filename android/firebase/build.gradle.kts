plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.ksp)
}

base.archivesName.set("android-firebase")

android {
    namespace = "net.sigmabeta.sage.perf.analytics.firebase"
}

dependencies {
    api(projects.common.perf)
    api(platform(libs.firebase.bom))
    api(libs.firebase.performance)
    implementation(projects.android.coroutines)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}
