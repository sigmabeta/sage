plugins {
    alias(libs.plugins.sage.jvm)
}


dependencies {
    api(projects.common.coroutines)
    implementation(projects.common.settings.general)
}
