plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.compose.compiler)
}

base.archivesName.set("android-ui-fonts")

android {
    namespace = "net.sigmabeta.sage.fonts"
}

dependencies {
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.material3)
}
