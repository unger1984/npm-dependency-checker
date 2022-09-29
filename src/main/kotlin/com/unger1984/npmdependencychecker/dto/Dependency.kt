package com.unger1984.npmdependencychecker.dto

data class Dependency(
    val packageName: String,
    val currentVersion: String,
    val index: Int
)
