@file:Suppress("UnusedImport") // Needed for delegates imports to not be removed by the IDE.
package extensions

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinCommonCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinOnlyTarget

inline fun KotlinMultiplatformExtension.androidWithPublication(
    project: Project,
    crossinline configure: KotlinAndroidTarget.() -> Unit = { }
) = android {
    publishLibraryVariants("release", "debug")
    mavenPublication { artifactId = "${Config.libraryProps.libraryName}-android" }; configure()
}

inline fun KotlinMultiplatformExtension.metadataPublication(
    project: Project,
    crossinline configure: KotlinOnlyTarget<KotlinCommonCompilation>.() -> Unit = { }
) = metadata {
    mavenPublication { artifactId = "${Config.libraryProps.libraryName}-common" }; configure()
}