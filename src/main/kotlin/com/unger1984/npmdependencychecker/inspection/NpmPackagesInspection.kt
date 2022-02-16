package com.unger1984.npmdependencychecker.inspection

import com.unger1984.npmdependencychecker.quickfix.UpdateDependencyQuickFix
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.json.psi.JsonElementVisitor
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.psi.PsiElementVisitor
import com.unger1984.npmdependencychecker.util.Latest
import com.unger1984.npmdependencychecker.util.NpmChecker
import kotlinx.coroutines.runBlocking


class NpmPackagesInspection : LocalInspectionTool() {
    private val checker = NpmChecker()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return PackageJsonElementVisitor(holder, isOnTheFly, checker)
    }
}

class PackageJsonElementVisitor(
    private val holder: ProblemsHolder,
    private val isOnTheFly: Boolean,
    private val checker: NpmChecker
) : JsonElementVisitor() {

    override fun visitObject(o: JsonObject) {
        if (!isOnTheFly) return
        val properties = o.propertyList.filter { it.name == "dependencies" || it.name == "devDependencies" }

        val dependencies: MutableList<Latest> = mutableListOf();
        for(property in properties){
            dependencies.addAll((property.value as JsonObject).propertyList.filter { it != null && it.value != null && it.value is JsonStringLiteral }
                .map {
                    Latest(it, it.name, (it.value as JsonStringLiteral).value)
                })
        }

        if(dependencies.isEmpty()) return

        runBlocking {
            val latestDependencies = checker.checkDependencies(dependencies)
            for (dependency in latestDependencies) {
                val psi = (dependency.pkg.value as JsonStringLiteral)
                holder.registerProblem(
                    psi,
                    "Latest available version is: ${dependency.version}",
                    ProblemHighlightType.LIKE_UNKNOWN_SYMBOL,
                    UpdateDependencyQuickFix(psi, dependency.version, 3),
                    UpdateDependencyQuickFix(psi, "~${dependency.version}", 2),
                    UpdateDependencyQuickFix(psi, "^${dependency.version}", 1)
                )
            }
        }
    }
}
