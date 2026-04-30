plugins {
    alias(libs.plugins.sage.jvm)
    alias(libs.plugins.ksp)
}

base.archivesName.set("common-appcomm")

dependencies {
    implementation(libs.hilt.core)
    implementation(libs.moshi)
    implementation(projects.common.logging)

    ksp(libs.hilt.compiler)
    ksp(libs.moshi.codegen)
}
