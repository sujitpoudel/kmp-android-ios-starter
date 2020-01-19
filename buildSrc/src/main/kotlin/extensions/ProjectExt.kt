@file:Suppress("UnusedImport") // Needed for delegates imports to not be removed by the IDE.
package extensions

import org.gradle.api.Project

fun Project.checkNoVersionRanges() {
    configurations.forEach { it ->
        it.resolutionStrategy {
            eachDependency {
                val version = requested.version ?: return@eachDependency
                check('+' !in version) {
                    "Version ranges are forbidden because they would make builds non reproducible."
                }
                check("SNAPSHOT" !in version) {
                    "Snapshot versions are forbidden because they would make builds non reproducible."
                }
            }
        }
    }

}
