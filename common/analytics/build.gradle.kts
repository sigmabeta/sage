plugins {
    alias(libs.plugins.sage.jvm)
}

base.archivesName.set("common-analytics")

dependencies {
    api("net.sigmabeta.sage:common-appcomm")
}
