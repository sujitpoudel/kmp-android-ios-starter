@file:Suppress("UnusedImport") // Needed for delegates imports to not be removed by the IDE.
package extensions

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Action

fun LibraryExtension.setDefaults() {
    compileSdkVersion(Config.android.compileSdkVersion)
    defaultConfig {
        minSdkVersion(Config.android.minSdkVersion)
        targetSdkVersion(Config.android.targetSdkVersion)
        versionCode = 1
        versionName = ProjectVersions.thisLibrary
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            matchingFallbacks = listOf("debug", "release")
        }
        getByName("debug") {
            isMinifyEnabled = false
            matchingFallbacks = listOf("debug", "release")
        }
    }
    sourceSets.getByName("main") {
        manifest.srcFile("src/main/AndroidManifest.xml")
        res.srcDir("src/main/res")
    }
    // TODO replace with https://issuetracker.google.com/issues/72050365 once released.
    libraryVariants.all(Action {
        generateBuildConfigProvider.configure {
            enabled = false
        }
    })
}

