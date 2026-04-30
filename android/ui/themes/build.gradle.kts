plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.compose.compiler)
}


android {
    namespace = "net.sigmabeta.sage.themes"
}

dependencies {
    api(libs.material)
    api(projects.android.ui.colors)
    api(projects.android.ui.fonts)
    implementation(libs.androidx.core.splash)
    implementation(libs.androidx.compose.ui.tooling.preview)
}
