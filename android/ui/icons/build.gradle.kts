plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.compose.compiler)
}

base.archivesName.set("android-ui-icons")

android {
    namespace = "net.sigmabeta.sage.ui.icons"
}

dependencies {
    implementation(projects.android.ui.themes)
    implementation(projects.common.ui.icons)
}
