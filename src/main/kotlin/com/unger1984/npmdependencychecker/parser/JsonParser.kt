package com.unger1984.npmdependencychecker.parser

import com.unger1984.npmdependencychecker.dto.DependencyDescription
import com.unger1984.npmdependencychecker.util.VersionsRepository
import com.unger1984.npmdependencychecker.util.getDependencies

class JsonParser(
    private val fileContent: String,
    private val versionsRepository: VersionsRepository
) {

    fun inspectFile(): List<DependencyDescription> {
        return getDependencyList()
    }

    private fun getDependencyList(): List<DependencyDescription> {
        val dependencies: List<DependencyDescription> = getAllDependenciesList()
        return getNotMatchingDependenciesList(dependencies)
    }

    private fun getAllDependenciesList(): List<DependencyDescription> {
        return fileContent.getDependencies().mapNotNull {
            try {
                val latestVersion = versionsRepository.getLatestVersion(it.packageName)
                DependencyDescription(it, latestVersion)
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun getNotMatchingDependenciesList(dependencies: List<DependencyDescription>): List<DependencyDescription> {
        return dependencies.mapNotNull {
            if (it.latestVersion != it.dependency.currentVersion) it else null
        }
    }
}
