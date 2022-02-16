package com.unger1984.npmdependencychecker.quickfix

import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.json.psi.JsonElementGenerator
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.jetbrains.annotations.NotNull

class UpdateDependencyQuickFix(element: PsiElement, @NotNull with: String, @NotNull order: Int) : LocalQuickFixOnPsiElement(element) {
    private val _with: String = with
    private val _order: Int = order

    override fun getFamilyName() = "$_order. Replace with $_with"
    override fun getText() = "$_order. Replace with $_with"

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        val nelement = JsonElementGenerator(project).createStringLiteral(_with)
        startElement.replace(nelement)
    }

}
