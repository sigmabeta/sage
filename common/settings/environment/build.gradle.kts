plugins {
    alias(libs.plugins.sage.jvm)
}

base.archivesName.set("common-settings-environment")

dependencies {
    api(projects.common.coroutines)
    api(projects.common.storage.common)
}
