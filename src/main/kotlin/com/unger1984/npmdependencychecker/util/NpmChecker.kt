package com.unger1984.npmdependencychecker.util

import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class NpmChecker {

    private val versionsRepository: VersionsRepository = VersionsRepository()
    private val parentJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + parentJob)

    suspend fun checkDependencies(allDependencies: List<Latest>): List<Latest> {
        parentJob.cancelChildren(cause = null)
        return getLatestDependencies(allDependencies)
    }

    private suspend fun getLatestDependencies(allDependencies: List<Latest>): List<Latest>{
        return allDependencies.map {
            scope.async {

                    val latest = versionsRepository.getLatestVersion(it.pkgName)
//                    val latest = runCommand("npm show ${it.pkgName} version")?.trim()
                    var res: Latest? = null
                    if (latest.isNotEmpty() && !it.version.endsWith(latest)) {
                        res = Latest(pkg = it.pkg, pkgName = it.pkgName, version = latest)
                    }
                    res
            }
        }.awaitAll().filterNotNull()
    }

    @Deprecated("Unused")
    private fun runCommand(cmd: String): String? {
        return try {
            val parts = cmd.split("\\s".toRegex())
            val proc = ProcessBuilder(*parts.toTypedArray())
                .directory(File("."))
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            proc.waitFor(1, TimeUnit.MINUTES)
            proc.inputStream.bufferedReader().readText()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
