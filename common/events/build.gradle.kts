plugins {
    alias(libs.plugins.sage.jvm)
}

base.archivesName.set("common-events")

dependencies {
    api(projects.common.analytics)
    api(projects.common.appcomm)
}
