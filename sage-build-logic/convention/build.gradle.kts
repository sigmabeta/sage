import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "net.sigmabeta.sage.buildlogic"

// Build-logic only — runs inside the Gradle daemon (JDK 21). Targeting 21 here lets the
// convention classpath consume Gradle plugins published for Java 21 (e.g. Paparazzi 2.0.0-alpha).
// This does NOT change app/module targets, which the convention plugins still set to JVM 17.
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.kotlin.compose.compiler.gradlePlugin)
    // implementation, not compileOnly: SageFeatureApiPlugin calls
    // pluginManager.apply("org.jetbrains.kotlin.plugin.serialization"), so the plugin's classes
    // have to be on the convention plugin's runtime classpath, not just its compile classpath.
    implementation(libs.kotlin.serialization.gradlePlugin)
    // implementation, not compileOnly: SageDiModulePlugin / SageFeatureRealPlugin call
    // pluginManager.apply("dev.zacsweers.metro"), so the plugin's classes have to be on
    // the convention plugin's runtime classpath, not just its compile classpath.
    implementation(libs.metro.gradlePlugin)
    // Not compileOnly: SageScreenshotModulePlugin applies "app.cash.paparazzi" by id, so the
    // plugin must be on the build-logic runtime classpath (unlike AGP/Kotlin, which consuming
    // modules already bring via their own plugins blocks).
    implementation(libs.paparazzi)
    // implementation, not compileOnly: SageComposeUiTestModulePlugin applies "org.jetbrains.compose"
    // by id AND reads its ComposePlugin.Dependencies (compose.uiTest / desktop.currentOs), so the
    // plugin classes must be on the convention plugin's runtime classpath.
    implementation(libs.compose.multiplatform.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("sageAndroid") {
            id = "sage.android"
            implementationClass = "SageAndroidModulePlugin"
            version = "1.0"
        }

        register("sageJvm") {
            id = "sage.jvm"
            implementationClass = "SageJvmModulePlugin"
            version = "1.0"
        }

        register("sageKmp") {
            id = "sage.kmp"
            implementationClass = "SageKmpModulePlugin"
            version = "1.0"
        }

        register("sageKmpJs") {
            id = "sage.kmp.js"
            implementationClass = "SageKmpJsPlugin"
            version = "1.0"
        }

        register("sageComposeAndroid") {
            id = "sage.compose.android"
            implementationClass = "SageComposeAndroidModulePlugin"
            version = "1.0"
        }

        register("sageComposeKmp") {
            id = "sage.compose.kmp"
            implementationClass = "SageComposeKmpModulePlugin"
            version = "1.0"
        }

        register("sageComposeUiTest") {
            id = "sage.compose.uitest"
            implementationClass = "SageComposeUiTestModulePlugin"
            version = "1.0"
        }

        register("sageDi") {
            id = "sage.di"
            implementationClass = "SageDiModulePlugin"
            version = "1.0"
        }

        register("sageDiWorkerAndroid") {
            id = "sage.di.worker.android"
            implementationClass = "SageDiWorkerAndroidModulePlugin"
            version = "1.0"
        }

    }
}
