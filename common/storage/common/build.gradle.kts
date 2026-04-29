plugins {
    alias(libs.plugins.sage.jvm)
}

base.archivesName.set("common-storage-common")

dependencies {
    api("net.sigmabeta.sage:common-coroutines")
}
