plugins {
    alias(libs.plugins.sage.android)
}

base.archivesName.set("android-connectivity")

android {
    namespace = "net.sigmabeta.sage.connectivity"
}

dependencies {
    api(projects.common.connectivity)
    implementation(projects.common.coroutines)
    implementation(projects.common.logging)
}
