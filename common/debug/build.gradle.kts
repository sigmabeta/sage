plugins {
    alias(libs.plugins.sage.jvm)
}

base.archivesName.set("common-debug")

dependencies {
    api(projects.common.coroutines)
    implementation(projects.common.settings.general)
}
