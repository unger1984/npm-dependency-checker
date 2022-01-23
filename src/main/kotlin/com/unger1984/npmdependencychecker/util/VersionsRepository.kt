package com.unger1984.npmdependencychecker.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.unger1984.npmdependencychecker.dto.Response
import java.io.IOException

const val NPM_API_URL = "https://registry.npmjs.org/"

class VersionsRepository {

    private val httpClient = DependencyHttpClient()
    private val cachedDependenciesList = mutableListOf<Dependency>()

    private val mapper = ObjectMapper()
        .registerKotlinModule()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun getLatestVersion(packageName: String): String {
        val cachedDependency = cachedDependenciesList.find { it.pkgName == packageName }
        return if(cachedDependency != null){
            cachedDependency.version
        }else{
            val version = fetchDependencyVersion(packageName);
            cachedDependenciesList.add(Dependency(packageName, version))
            version
        }
    }

    private fun fetchDependencyVersion(packageName: String): String {
        try {
            val jsonResponse = httpClient.getContentAsString(NPM_API_URL + packageName)
            val response = parseResponse(jsonResponse)
            return response.distTags.latest.trim()
        } catch (e: IOException) {
            return ""
        }
    }

    private fun parseResponse(responseString: String): Response {
        return mapper.readValue(responseString)
    }

}

private data class Dependency(
    val pkgName: String,
    val version: String
)
