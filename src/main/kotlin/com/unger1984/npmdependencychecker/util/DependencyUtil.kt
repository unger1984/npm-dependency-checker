package com.unger1984.npmdependencychecker.util

import com.unger1984.npmdependencychecker.dto.Dependency

const val DEPENDENCIES_PATTERN =
    """^"(\S+)?"(\s+)?:(\s+)?"(([^~])?[0-9]+\.[0-9]+\.[0-9]+\+?\S*)",$"""

fun String.getDependencies(): List<Dependency> {
    val dependencyList = mutableListOf<Dependency>()
    var line = ""
    var counter = 0
    var isDependency = false

    forEach {
        counter++
        if (it == '\n') {
            line = line.trim()
            if(line.contains("\"dependencies\":") || line.contains("\"devDependencies\"")) {
                isDependency = true
            }else if(line.contains("}")){
                isDependency = false;
//                line += it
            }else if (isDependency) {
                val packageName = line.getPackageName()
                val currentVersion = line.getVersionName()
                dependencyList.add(Dependency(packageName, currentVersion, counter - 4))
                printMessage("Found dependency: $line $packageName $currentVersion")
            }
            line = ""
        } else {
            line += it
        }
    }
    return dependencyList
}

fun String.getPackageName(): String {
    val regex = DEPENDENCIES_PATTERN.toRegex()
    return try {
        regex.find(this)?.groupValues?.get(1)!!
    } catch (e: Exception) {
        "";
    }
}

fun String.getVersionName(): String {
    val regex = DEPENDENCIES_PATTERN.toRegex()
    return try {
        regex.find(this)?.groupValues?.get(4)!!
    } catch (e: Exception) {
        "";
    }
}
