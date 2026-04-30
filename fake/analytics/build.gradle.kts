plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.ksp)
}


android {
    namespace = "net.sigmabeta.sage.fake.analytics"
}

dependencies {
    api(projects.common.analytics)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}
