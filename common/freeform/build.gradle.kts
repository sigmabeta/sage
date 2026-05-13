plugins {
    alias(libs.plugins.sage.jvm)
}

dependencies {
    api(projects.common.appcomm)
    api(projects.common.ui.strings)
    api(projects.common.ui.components)
}
