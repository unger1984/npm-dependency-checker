package com.unger1984.npmdependencychecker.util

import com.intellij.openapi.diagnostic.Logger
import com.unger1984.npmdependencychecker.inspection.NpmPackagesInspection

private val LOG = Logger.getInstance(NpmPackagesInspection::class.java)

fun printMessage(message: String) {
    println(message)
    LOG.info(message)
}
