plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "net.sigmabeta.sage.perf"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(projects.common.logging)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
}
