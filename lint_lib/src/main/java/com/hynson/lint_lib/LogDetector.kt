package com.hynson.lint_lib

import com.android.tools.lint.detector.api.*
import com.android.tools.lint.detector.api.Category.Companion.SECURITY
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

class LogDetector : Detector(), Detector.UastScanner {
    override fun getApplicableMethodNames(): List<String>? {
        return listOf("v", "d", "i", "w", "e")
    }

    /*override fun visitMethod(
        context: JavaContext,
        visitor: JavaElementVisitor?,
        call: PsiMethodCallExpression,
        method: PsiMethod
    ) {
        if (context.evaluator.isMemberInClass(method, "android.util.Log")) {
            context.report(ISSUE, call, context.getLocation(call), "请勿直接调用android.util.Log，应该使用统一Log工具类");
        }
    }*/

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        super.visitMethodCall(context, node, method)
        println("LogDetector:visitMethodCall ${method.name} ${context.getLocation(node).file.absolutePath}")
        if (context.evaluator.isMemberInClass(method, "android.util.Log")) {
            context.report(
                ISSUE,
                node,
                context.getLocation(node),
                "请勿直接调用android.util.Log，应该使用统一工具类"
            )
        }
    }

    companion object{
        var ISSUE: Issue = Issue.create(
            "LogUsage",
            "禁止使用该方法", "Android.util.log()",
            SECURITY, 5, Severity.INFORMATIONAL,
            Implementation(LogDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }
}