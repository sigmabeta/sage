plugins {
    alias(libs.plugins.sage.jvm)
}


dependencies {
    api(projects.common.analytics)
    api(projects.common.appcomm)
}
