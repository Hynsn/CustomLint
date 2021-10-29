package com.hynson.lint_lib

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.*
import org.jetbrains.uast.util.isConstructorCall
import org.jetbrains.uast.util.isMethodCall

class AnnotationDetector : Detector() ,Detector.UastScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(UAnnotation::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitAnnotation(node: UAnnotation) {
                isAnnotation(node)
            }

            private fun isAnnotation(node: UAnnotation) {
                when(node.qualifiedName){
                    ANNOTATION -> {
                        context.report(
                            ISSUE1, node, context.getLocation(node),
                            "该注解不允许使用"
                        )
                    }
                    AROUTER_ANNOTATION -> {
                        context.report(
                            ISSUE2, node, context.getLocation(node),
                            "该注解不允许使用"
                        )
                    }
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
                            ISSUE1, node, context.getLocation(node),
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
                                ISSUE1, node, context.getLocation(node),
                                "ffff"
                            )
                        }
                    }
                }
            }
        }

    }

    companion object {
        /* butterknife */
        const val PACKAGE = "butterknife"
        const val ANNOTATION = "$PACKAGE.BindView"
        const val CALL = "$PACKAGE.ButterKnife"

        var ISSUE1 = Issue.create(
            "ButterKnifeUsage",
            "禁止使用ButterKnife",
            "项目中不可以使用ButterKnife,请统一使用DataBinding或ViewBinding!",
            Category.CORRECTNESS,
            6,
            Severity.WARNING,
            Implementation(AnnotationDetector::class.java,Scope.JAVA_FILE_SCOPE)
        )

        /* arouter */
        private const val AROUTER_PACKAGE = "com.alibaba.android.arouter.facade"
        private val AROUTER_ANNOTATION: String = AROUTER_PACKAGE + ".annotation.Route"
        private val WM_ROUTER_CALL: String = AROUTER_PACKAGE + ".Router"
        val ISSUE2: Issue = Issue.create(
            "ARouterUsage",
            "禁止使用ARouter",
            "项目中不可以随意使用ARouter",
            Category.CORRECTNESS,
            6,
            Severity.WARNING,
            Implementation(AnnotationDetector::class.java,Scope.JAVA_FILE_SCOPE)
        )
    }
}