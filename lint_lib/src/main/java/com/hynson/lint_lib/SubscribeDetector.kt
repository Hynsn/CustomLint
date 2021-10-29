package com.hynson.lint_lib

import com.android.tools.lint.detector.api.*
import com.android.tools.lint.detector.api.Category.Companion.SECURITY
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.TypeConversionUtil
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement

/**
 * Author: Hynsonhou
 * Date: 2021/10/25 19:25
 * Description: rxjava onError检测
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2021/10/25   1.0       首次创建
 */
class SubscribeDetector : Detector() ,Detector.UastScanner {

    private val check = RxJavaSubscriberCheck()

    override fun getApplicableMethodNames(): List<String>? {
        return arrayListOf("subscribe")
    }

    private fun report(context: JavaContext,node:UElement){
        context.report(ISSUE,node,context.getLocation(node),"Subscriber is missing onError")
    }

    override fun visitMethod(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if(check.isMissOnError(method)){
            report(context,node)
        }
    }

    companion object {
        val ISSUE = Issue.create("RxSubscribeOnError", "Subscriber is missing onError handling",
            "Every Observable stream can report errors that should be handled using onError. Not implementing onError will throw an exception at runtime which can be hard to debug when the error is thrown on a Scheduler that is not the invoking thread.",
            SECURITY, 8, Severity.INFORMATIONAL,
            Implementation(SubscribeDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        val RX_OBSERVABLE = "rx.Observable"
        val RX_SINGLE = "rx.Single"
        val RX_COMPLETABLE = "rx.Completable"

        val RX_ACTION1 = "rx.functions.Action1"
        val RX_ACTION0 = "rx.functions.Action0"
    }

    interface SubscriberCheck{
        fun isMissOnError(method: PsiMethod) : Boolean
    }

    class RxJavaSubscriberCheck : SubscriberCheck{

        override fun isMissOnError(method: PsiMethod): Boolean {
            val clz: PsiClass? = method.containingClass
            clz?.qualifiedName.let {
                return when(it){
                    RX_OBSERVABLE,RX_SINGLE ->{
                        (method.parameterList.parametersCount==0) ||
                                (method.parameterList.parametersCount==1 &&
                                        TypeConversionUtil.erasure(method.parameterList.parameters[0].type).equalsToText(RX_ACTION1))
                    }
                    RX_COMPLETABLE -> {
                        (method.parameterList.parametersCount==0) ||
                                (method.parameterList.parametersCount==1 &&
                                        TypeConversionUtil.erasure(method.parameterList.parameters[0].type).equalsToText(RX_ACTION0))
                    }
                    else -> false
                }
            }
        }
    }
}