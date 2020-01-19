@file:Suppress("UnusedImport") // Needed for delegates imports to not be removed by the IDE.
package extensions

import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import java.util.*

fun KotlinTarget.configureMavenPublication() {
    val suffix = when (platformType) {
        KotlinPlatformType.common -> "-metadata"
        KotlinPlatformType.jvm -> ""
        KotlinPlatformType.js -> "-js"
        KotlinPlatformType.androidJvm -> ""
        KotlinPlatformType.native -> "-${name.toLowerCase(Locale.ROOT)}"
    }
    mavenPublication {
        val prefix = Config.libraryProps.libraryName
        artifactId = "$prefix-${project.name}$suffix"
        if (platformType == KotlinPlatformType.androidJvm) {
            // We disable metadata generation for Android publications, so the release variants can
            // be used for any buildType of the consumer projects without having to specify
            // matchingFallbacks unless the multiplatform artifact is used.
            val capitalizedPublicationName = "${name.first().toTitleCase()}${name.substring(1)}"
            val metadataTaskName = "generateMetadataFileFor${capitalizedPublicationName}Publication"
            project.tasks.named(metadataTaskName) { onlyIf { false } }
        }
    }
    if (platformType == KotlinPlatformType.androidJvm) {
        (this as KotlinAndroidTarget).publishLibraryVariants("release", "debug")
        // Relies on metadata to be disabled (done above) to avoid buildType mismatch on consumers.
    }
}
