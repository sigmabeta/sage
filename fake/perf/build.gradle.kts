plugins {
    alias(libs.plugins.sage.jvm)
}

base.archivesName.set("fake-perf")

dependencies {
    api(projects.common.perf)
}
