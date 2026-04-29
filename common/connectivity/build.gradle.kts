plugins {
    alias(libs.plugins.sage.jvm)
}

base.archivesName.set("common-connectivity")

dependencies {
    implementation("net.sigmabeta.sage:common-coroutines")
}
