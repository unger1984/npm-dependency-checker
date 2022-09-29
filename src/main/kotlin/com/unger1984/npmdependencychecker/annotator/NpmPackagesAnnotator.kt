package com.unger1984.npmdependencychecker.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.application.runReadAction
import com.intellij.psi.PsiFile
import com.unger1984.npmdependencychecker.dto.DependencyDescription
import com.unger1984.npmdependencychecker.parser.JsonParser
import com.unger1984.npmdependencychecker.quickfix.UpdateDependencyQuickFix
import com.unger1984.npmdependencychecker.util.DependencyHttpClient
import com.unger1984.npmdependencychecker.util.VersionsRepository

class NpmPackagesAnnotator : ExternalAnnotator<NpmPackagesAnnotator.Info, NpmPackagesAnnotator.Result>() {
    data class Info(val file: PsiFile)
    data class Result(val annotations: List<DependencyDescription>)

    override fun collectInformation(file: PsiFile): Info =
        runReadAction { Info(file) }

    override fun doAnnotate(collectedInfo: Info?): Result? {
        if (collectedInfo == null) return null

        val httpClient = DependencyHttpClient()
        val versionsRepository = VersionsRepository(httpClient)
        val yamlParser = JsonParser(collectedInfo.file.text, versionsRepository)
        val annotations = yamlParser.inspectFile()
        return if (annotations.isNotEmpty()) Result(annotations) else null
    }

    override fun apply(file: PsiFile, annotationResult: Result?, holder: AnnotationHolder) {
        if (annotationResult == null) return

        annotationResult.annotations.forEach {
            val psiElement = file.findElementAt(it.dependency.index)!!
            holder.newAnnotation(HighlightSeverity.WARNING, "New version ${it.latestVersion}")
                .range(psiElement)
                .newFix(UpdateDependencyQuickFix(psiElement, it.latestVersion, 3))
                .registerFix()
                .newFix(UpdateDependencyQuickFix(psiElement, "~${it.latestVersion}", 2))
                .registerFix()
                .newFix(UpdateDependencyQuickFix(psiElement, "^${it.latestVersion}", 1))
                .registerFix()
                .create()
        }
    }
}
