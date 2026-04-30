plugins {
    alias(libs.plugins.sage.android)
}


android {
    namespace = "net.sigmabeta.sage.wakelocks"
}

dependencies {
    implementation(libs.androidx.activity)
    api(projects.common.wakelocks)
    implementation(projects.common.appcomm)
    implementation(projects.common.coroutines)
    implementation(projects.common.ui.strings)
}
