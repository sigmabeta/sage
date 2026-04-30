plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.ksp)
}

base.archivesName.set("android-bitmaps")

android {
    namespace = "net.sigmabeta.sage.bitmaps"
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(projects.android.ui.fonts)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}
