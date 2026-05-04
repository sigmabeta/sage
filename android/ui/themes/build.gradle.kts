plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "net.sigmabeta.sage.themes"
}

dependencies {
    api(libs.material)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.material3)
    implementation(libs.androidx.core.splash)
    implementation(libs.androidx.compose.ui.tooling.preview)
}
