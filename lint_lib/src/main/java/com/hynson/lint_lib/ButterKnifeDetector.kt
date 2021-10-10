package com.hynson.lint_lib

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.*
import org.jetbrains.uast.util.isConstructorCall
import org.jetbrains.uast.util.isMethodCall

class ButterKnifeDetector : Detector() ,Detector.UastScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>>? {
        return listOf(UAnnotation::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {
            override fun visitAnnotation(node: UAnnotation) {
                isAnnotation(node)
            }

            private fun isAnnotation(node: UAnnotation) {
                val type = node.qualifiedName
                if (ANNOTATION == type) {
                    context.report(
                        ARouterDetector.ISSUE, node, context.getLocation(node),
                        "该注解不允许使用"
                    )
                }
            }

            override fun visitCallExpression(node: UCallExpression) {
                /*checkIsMethod(node);
                checkIsConstructorCall(node);*/
            }

            private fun checkIsConstructorCall(node: UCallExpression) {
                if (!node.isConstructorCall()) {
                    return
                }
                val classRef = node.classReference
                if (classRef != null) {
                    val className = classRef.getQualifiedName()
                    val uriValue = "$PACKAGE.common.DefaultUriRequest"
                    val pageValue = "$PACKAGE.common.DefaultPageUriRequest"
                    if (className == uriValue || className == pageValue) {
                        context.report(
                            ARouterDetector.CALL_ISSUE, node, context.getLocation(node),
                            "ggtt "
                        )
                    }
                }
            }

            private fun checkIsMethod(node: UCallExpression) {
                if (node.isMethodCall()) {
                    if (node.receiver != null && node.methodName != null) {
                        val method = node.resolve()
                        if (context.evaluator.isMemberInClass(method, CALL)) {
                            context.report(
                                ARouterDetector.CALL_ISSUE, node, context.getLocation(node),
                                "ffff"
                            )
                        }
                    }
                }
            }
        }

    }

    companion object {
        var ISSUE = Issue.create(
            "ButterKnife",
            "禁止使用",
            "项目中不可以使用ButterKnife,请统一使用DataBinding或ViewBinding!",
            Category.CORRECTNESS,
            6,
            Severity.WARNING,
            Implementation(ButterKnifeDetector::class.java,Scope.JAVA_FILE_SCOPE)
        )
        const val PACKAGE = "butterknife"
        const val ANNOTATION = "$PACKAGE.BindView"
        const val CALL = "$PACKAGE.ButterKnife"
    }
}