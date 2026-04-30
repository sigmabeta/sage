plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.ksp)
}

base.archivesName.set("android-coroutines")

android {
    namespace = "net.sigmabeta.sage.coroutines"
}

dependencies {
    api(projects.common.coroutines)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}
