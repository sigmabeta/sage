plugins {
    alias(libs.plugins.sage.android)
}

android {
    namespace = "net.sigmabeta.sage.connectivity"
}

dependencies {
    api(projects.common.connectivity)
    implementation(projects.common.coroutines)
    implementation(projects.common.logging)
}
