plugins {
    alias(libs.plugins.sage.jvm)
}

dependencies {
    implementation(libs.moshi)
    implementation(projects.common.connectivity)
}
