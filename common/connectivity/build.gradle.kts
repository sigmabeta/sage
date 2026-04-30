plugins {
    alias(libs.plugins.sage.jvm)
}

base.archivesName.set("common-connectivity")

dependencies {
    implementation(projects.common.coroutines)
}
