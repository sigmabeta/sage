plugins {
    alias(libs.plugins.sage.android)
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
}
