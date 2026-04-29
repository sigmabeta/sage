plugins {
    alias(libs.plugins.sage.jvm)
}

base.archivesName.set("common-coroutines")

dependencies {
    api(libs.kotlinx.coroutines.core)
}
