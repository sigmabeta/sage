plugins {
    alias(libs.plugins.sage.android)
}

android {
    namespace = "net.sigmabeta.sage.bitmaps"
}

dependencies {
    api(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.sage.common.logging)
}
