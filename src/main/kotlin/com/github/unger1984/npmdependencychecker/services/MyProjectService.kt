package com.github.unger1984.npmdependencychecker.services

import com.intellij.openapi.project.Project
import com.github.unger1984.npmdependencychecker.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
