@file:Suppress("UnusedImport") // Needed for delegates imports to not be removed by the IDE.
package extensions

import org.gradle.api.artifacts.repositories.MavenArtifactRepository

fun MavenArtifactRepository.ensureGroups(vararg group: String): MavenArtifactRepository = apply {
    content {
        group.forEach {
            includeGroup(it)
        }
    }
}

fun MavenArtifactRepository.ensureGroupsByRegexp(vararg regexp: String): MavenArtifactRepository = apply {
    content {
        regexp.forEach {
            includeGroupByRegex(it)
        }
    }
}

fun MavenArtifactRepository.ensureGroupsStartingWith(vararg regexp: String): MavenArtifactRepository = apply {
    content {
        regexp.forEach {
            includeGroupByRegex(it.replace(".", "\\.") + ".*")
        }
    }
}

fun MavenArtifactRepository.ensureModules(vararg modules: String): MavenArtifactRepository = apply {
    content {
        modules.forEach {
            includeModule(it.substringBefore(':'), it.substringAfter(':'))
        }
    }
}

fun MavenArtifactRepository.ensureModulesByRegexp(vararg regexp: String): MavenArtifactRepository = apply {
    content {
        regexp.forEach {
            includeModuleByRegex(it.substringBefore(':'), it.substringAfter(':'))
        }
    }
}

fun MavenArtifactRepository.ensureModulesStartingWith(vararg regexp: String): MavenArtifactRepository = apply {
    content {
        regexp.forEach {
            val groupRegex = it.substringBefore(':').replace(".", "\\.")
            val moduleNameRegex = it.substringAfter(':').replace(".", "\\.") + ".*"
            includeModuleByRegex(groupRegex, moduleNameRegex)
        }
    }
}

