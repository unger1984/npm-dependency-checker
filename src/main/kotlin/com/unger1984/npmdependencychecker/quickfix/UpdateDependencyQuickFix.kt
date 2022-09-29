package com.unger1984.npmdependencychecker.quickfix

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.util.IncorrectOperationException
import org.jetbrains.annotations.NotNull

class UpdateDependencyQuickFix(private val element: PsiElement, @NotNull with: String, @NotNull order: Int) : BaseIntentionAction() {
    private val _with: String = with
    private val _order: Int = order

    override fun getFamilyName() = "$_order. Replace with $_with"
    override fun getText() = "$_order. Replace with $_with"

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
        return true
    }

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val factory = JavaPsiFacade.getElementFactory(project)
        val iElementType = IElementType("text", Language.findLanguageByID("JSON"))
        val psiExpression = factory.createDummyHolder("^$_with", iElementType, null)
        element.replace(psiExpression)
    }
}
