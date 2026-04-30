plugins {
    alias(libs.plugins.sage.jvm)
}

base.archivesName.set("common-settings-general")

dependencies {
    api(projects.common.coroutines)
    api(projects.common.storage.common)
}
