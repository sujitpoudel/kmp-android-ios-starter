@file:Suppress("UnusedImport") // Needed for delegates imports to not be removed by the IDE.
package extensions

import org.gradle.api.publish.maven.MavenPublication

@Suppress("UnstableApiUsage")
fun MavenPublication.setupPom() = pom {
    name.set(Config.libraryProps.libraryName)
    description.set(Publishing.libraryDesc)
    url.set(Publishing.siteUrl)
    licenses {
        license {
            name.set("The Apache Software License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
        }
    }
    developers {
        developer {
            id.set("<set-id-here>")
            name.set("<set-name-here>")
            email.set("<set-email-here>")
        }
    }
    scm {
        connection.set(Publishing.gitUrl)
        developerConnection.set(Publishing.gitUrl)
        url.set(Publishing.siteUrl)
    }
}

