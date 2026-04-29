import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "net.sigmabeta.sage.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.kotlin.compose.compiler.gradlePlugin)
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

        register("sageComposeAndroid") {
            id = "sage.compose.android"
            implementationClass = "SageComposeAndroidModulePlugin"
            version = "1.0"
        }
    }
}
