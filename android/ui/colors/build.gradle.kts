plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.compose.compiler)
}

base.archivesName.set("android-ui-colors")

android {
    namespace = "net.sigmabeta.sage.colors"
}

dependencies {
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.material3)
}
