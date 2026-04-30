plugins {
    alias(libs.plugins.sage.jvm)
}

base.archivesName.set("common-list")

dependencies {
    api(libs.kotlinx.collections.immutable)
    api(projects.common.appcomm)
    api(projects.common.analytics)
    api(projects.common.coroutines)
    api(projects.common.logging)
    api(projects.common.nav)
    api(projects.common.ui.strings)
    implementation(projects.common.ui.components)
}
