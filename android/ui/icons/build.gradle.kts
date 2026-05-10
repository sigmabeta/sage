plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "net.sigmabeta.sage.ui.icons"
}

dependencies {
    implementation(projects.android.ui.themes)
    implementation(projects.common.ui.icons)

    // material-icons-core/extended are no longer in the Compose BOM (last released 1.7.8).
    api("androidx.compose.material:material-icons-core:1.7.8")
    api("androidx.compose.material:material-icons-extended:1.7.8")
}
