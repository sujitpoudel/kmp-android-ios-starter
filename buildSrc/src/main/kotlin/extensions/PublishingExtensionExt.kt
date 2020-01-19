@file:Suppress("UnusedImport") // Needed for delegates imports to not be removed by the IDE.
package extensions

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.withType

fun PublishingExtension.setupAllPublications(project: Project) {
    project.configurations.create("compileClasspath")
    //TODO: Remove line above when https://youtrack.jetbrains.com/issue/KT-27170 is fixed
    project.group = Config.libraryProps.group
    project.version = Config.libraryProps.version
    val publications = publications.withType<MavenPublication>()
    publications.all { setupPom() }
    publications.findByName("kotlinMultiplatform")?.apply {
        artifactId = "${Config.libraryProps.libraryName}-${project.name}-mpp"
    }
    if (publications.getAt("kotlinMultiplatform").artifactId == "${Config.libraryProps.libraryName}-mpp") {
        return
    }
    setupPublishRepo(project)
}

private fun PublishingExtension.setupPublishRepo(project: Project) {
    repositories {
        maven {
            name = "bintray"
            val bintrayUsername = "<Bintray username>"
            val bintrayRepoName = "<Bintray repo name>"
            val bintrayPackageName = "${Config.libraryProps.libraryName}"
            setUrl(
                "https://api.bintray.com/maven/" +
                        "$bintrayUsername/$bintrayRepoName/$bintrayPackageName/;" +
                        "publish=0;" +
                        "override=1"
            )
            credentials {
                username = project.findProperty("bintray_user") as String?
                password = project.findProperty("bintray_api_key") as String?
            }
        }
    }
}

