plugins {
    alias(libs.plugins.sage.jvm)
}

base.archivesName.set("common-ui-strings")

dependencies {
    implementation(libs.moshi)
    implementation("net.sigmabeta.sage:common-connectivity")
}
