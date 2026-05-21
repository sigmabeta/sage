plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.metro)
}

android {
    namespace = "net.sigmabeta.sage.coroutines"
}

metro {
    interop {
        includeDagger()
    }
}

dependencies {
    api(projects.common.coroutines)
    implementation(projects.common.di)
}
