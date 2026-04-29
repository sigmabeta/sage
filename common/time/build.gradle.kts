plugins {
    alias(libs.plugins.sage.jvm)
}

base.archivesName.set("common-time")

dependencies {
    api(libs.threeten)
}
