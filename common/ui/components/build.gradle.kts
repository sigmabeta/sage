plugins {
    alias(libs.plugins.sage.jvm)
}

dependencies {
    api(libs.kotlinx.collections.immutable)
    api(projects.common.appcomm)
    api(projects.common.images)
    api(projects.common.pdf)
    api(projects.common.ui.icons)
}
