plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.ksp)
}

base.archivesName.set("android-resources")

android {
    namespace = "net.sigmabeta.sage.resources"
}

dependencies {
    implementation(projects.android.coroutines)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}
