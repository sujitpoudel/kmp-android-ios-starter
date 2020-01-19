object Config {
    val buildPlugins = BuildPlugins
    val version = Versions
    val android = Android
    val libraryProps = LibraryProps
    val ios = Ios
    val common = Common
    val publishing = Publishing
}

object Versions {
    const val mavenGradle = "2.1"
    const val kotlin = "1.3.61"
    const val gradleTool = "3.5.2"
    const val coroutines = "1.3.3"
    const val serialization = "0.14.0"
    const val androidLifecycle = "2.0.0"
    const val bintray = "1.8.4"
    const val robolectric = "4.3"
    const val junit = "4.12"
    const val aws_sdk_core = "1.11.5"
    const val ktor = "1.3.0"
}

object ProjectVersions {
    const val thisLibrary = "1.0.0"
}

object BuildPlugins {
    const val multiplatform = "org.jetbrains.kotlin.multiplatform"
    const val androidLib = "com.android.library"
    const val mavenPublish = "maven-publish"
    const val androidx = "kotlin-android-extensions"
    const val serialization = "kotlinx-serialization"
}

object LibraryProps {
    const val group = "com.example.company"
    const val libraryName = "kmp-android-ios-starter"
    const val version = ProjectVersions.thisLibrary
}

object Android {
    const val minSdkVersion = 21
    const val targetSdkVersion = 29
    const val compileSdkVersion = 29
    val dependencies = Dependencies
    object Dependencies {
        const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Config.version.coroutines}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Config.version.coroutines}"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Config.version.serialization}"
        const val lifecycle = "androidx.lifecycle:lifecycle-extensions:${Config.version.androidLifecycle}"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Config.version.kotlin}"
        const val test = "org.jetbrains.kotlin:kotlin-test:${Config.version.kotlin}"
        const val testKotlinJunit = "org.jetbrains.kotlin:kotlin-test-junit:${Config.version.kotlin}"
        const val testJunit = "junit:junit:${Config.version.junit}"
        const val coroutines_test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Config.version.coroutines}"
        const val robolectricTest = "org.robolectric:robolectric:${Config.version.robolectric}"
        const val ktor = "io.ktor:ktor-client-android:${Config.version.ktor}"
        const val ktor_auth = "io.ktor:ktor-client-auth-jvm:${Config.version.ktor}"
    }
}

object Ios {
    const val frameworkName = "iOSFrameworkName"
    const val gradleTaskNameDebug = "debugFramework"
    const val gradleTaskNameRelease = "releaseFramework"
    val dependencies = Dependencies
    object Dependencies {
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${Config.version.coroutines}"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:${Config.version.serialization}"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Config.version.kotlin}"
        const val ktor = "io.ktor:ktor-client-ios:${Config.version.ktor}"
        const val ktor_auth = "io.ktor:ktor-client-auth-native:${Config.version.ktor}"
    }
}

object Common {
    val dependencies = Dependencies
    object Dependencies {
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${Config.version.coroutines}"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:${Config.version.serialization}"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-common:${Config.version.kotlin}"
        const val test = "test-common"
        const val testAnnotations = "test-annotations-common"
        const val ktor = "io.ktor:ktor-client-core:${Config.version.ktor}"
        const val ktor_auth = "io.ktor:ktor-client-auth:${Config.version.ktor}"
    }
}

object Publishing {
    const val gitUrl = "Github URL here"
    const val siteUrl = "Project Website URL"
    const val libraryDesc = "Describe this library"
}