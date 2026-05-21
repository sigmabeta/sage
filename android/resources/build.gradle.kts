plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.metro)
}

android {
    namespace = "net.sigmabeta.sage.resources"
}

metro {
    interop {
        includeDagger()
    }
}

dependencies {
    implementation(projects.common.di)
    implementation(projects.android.coroutines)
}
