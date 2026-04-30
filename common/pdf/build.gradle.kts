plugins {
    alias(libs.plugins.sage.jvm)
}

base.archivesName.set("common-pdf")

dependencies {
    implementation(projects.common.images)
}
