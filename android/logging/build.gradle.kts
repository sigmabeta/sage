plugins {
    alias(libs.plugins.sage.android)
}

base.archivesName.set("android-logging")

android {
    namespace = "net.sigmabeta.sage.logging"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(projects.common.logging)
}
