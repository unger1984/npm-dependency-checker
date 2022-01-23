package com.unger1984.npmdependencychecker.quickfix

import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.json.psi.JsonElementGenerator
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.jetbrains.annotations.NotNull

class UpdateDependencyQuickFix(element: PsiElement, @NotNull with: String) : LocalQuickFixOnPsiElement(element) {
    private val _with: String = with

    override fun getFamilyName() = "Replace with $_with"
    override fun getText() = "Replace with $_with"

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        val nelement = JsonElementGenerator(project).createStringLiteral(_with)
        startElement.replace(nelement)
    }

}
