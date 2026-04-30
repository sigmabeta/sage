plugins {
    alias(libs.plugins.sage.android)
}


android {
    namespace = "net.sigmabeta.sage.ui.strings"
}

dependencies {
    api(projects.common.ui.strings)
}
