plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.metro)
}

android {
    namespace = "net.sigmabeta.sage.fake.analytics"
}

metro {
    interop {
        includeDagger()
    }
}

dependencies {
    api(projects.common.analytics)
    implementation(projects.common.di)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}
