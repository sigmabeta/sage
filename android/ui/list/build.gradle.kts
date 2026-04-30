plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.compose.android)
}

android {
    namespace = "net.sigmabeta.sage.android.ui.list"
}

dependencies {
    api(libs.androidx.compose.foundation)
    api(libs.androidx.navigation.compose)

    api(projects.common.appcomm)
    api(projects.common.list)
    api(projects.common.nav)
    api(projects.common.ui.components)
}
