package com.github.lilittlecat.intellijpangu.services

import com.intellij.openapi.project.Project
import com.github.lilittlecat.intellijpangu.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
