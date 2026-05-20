plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.ksp)
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
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}
