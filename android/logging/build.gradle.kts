plugins {
    alias(libs.plugins.sage.android)
}


android {
    namespace = "net.sigmabeta.sage.logging"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(projects.common.logging)
}
