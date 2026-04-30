plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.ksp)
}

base.archivesName.set("android-analytics")

android {
    namespace = "net.sigmabeta.sage.analytics"
}

dependencies {
    api(projects.common.analytics)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(projects.android.coroutines)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}
