package com.unger1984.npmdependencychecker.util

import com.intellij.openapi.diagnostic.Logger
import com.unger1984.npmdependencychecker.annotator.NpmPackagesAnnotator

private val LOG = Logger.getInstance(NpmPackagesAnnotator::class.java)

fun printMessage(message: String) {
    println(message)
    LOG.info(message)
}
