plugins {
    alias(libs.plugins.sage.android)
}

base.archivesName.set("android-ui-strings")

android {
    namespace = "net.sigmabeta.sage.ui.strings"
}

dependencies {
    api(projects.common.ui.strings)
}
