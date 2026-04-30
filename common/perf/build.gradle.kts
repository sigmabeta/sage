plugins {
    alias(libs.plugins.sage.jvm)
}

base.archivesName.set("common-perf")

dependencies {
    api(projects.common.coroutines)
    api(projects.common.analytics)
}
