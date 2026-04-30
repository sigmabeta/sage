plugins {
    alias(libs.plugins.sage.jvm)
}

dependencies {
    api(projects.common.coroutines)
    api(projects.common.storage.common)
}
