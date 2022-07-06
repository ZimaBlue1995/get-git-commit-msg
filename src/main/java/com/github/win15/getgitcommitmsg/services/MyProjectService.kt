package com.github.win15.getgitcommitmsg.services

import com.intellij.openapi.project.Project
import com.github.win15.getgitcommitmsg.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
