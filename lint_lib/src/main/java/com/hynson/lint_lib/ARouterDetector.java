package com.hynson.lint_lib;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UReferenceExpression;
import org.jetbrains.uast.UastUtils;
import org.jetbrains.uast.util.UastExpressionUtils;

import java.util.ArrayList;
import java.util.List;
//com.alibaba.android.arouter.facade.annotation.Route
public class ARouterDetector extends Detector implements Detector.UastScanner {
    private final String PACKAGE = "com.alibaba.android.arouter.facade";
    private final String ANNOTATION = PACKAGE + ".annotation.Route";
    private final String WM_ROUTER_CALL = PACKAGE + ".Router";

    static final Issue ISSUE = Issue.create(
            "router_annotation_issue",    //唯一 ID
            "不允许使用该注解",
            "全局项目不允许使用该注解 请更换RouterUri",
            Category.CORRECTNESS,
            6,
            Severity.WARNING,
            new Implementation(
                    ARouterDetector.class,
                    Scope.JAVA_FILE_SCOPE));

    static final Issue CALL_ISSUE = ISSUE.create("router_call_issue",    //唯一 ID
            "不要直接引用WM router",
            "使用项目封装的路由中间件完成跳转",
            Category.CORRECTNESS,
            6,
            Severity.WARNING,
            new Implementation(
                    ARouterDetector.class,
                    Scope.JAVA_FILE_SCOPE));

    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        List<Class<? extends UElement>> types = new ArrayList<>();
        types.add(UAnnotation.class);
        types.add(UCallExpression.class);
        return types;
    }

    @Override
    public UElementHandler createUastHandler(@NotNull JavaContext context) {
        return new UElementHandler() {

            @Override
            public void visitAnnotation(@NotNull UAnnotation node) {
                isAnnotation(node);
            }

            private void isAnnotation(UAnnotation node) {
                String type = node.getQualifiedName();
                if (ANNOTATION.equals(type)) {
                    context.report(ISSUE, node, context.getLocation(node),
                            "该注解不允许使用");
                }
            }

            @Override
            public void visitCallExpression(@NotNull UCallExpression node) {
                /*checkIsMethod(node);
                checkIsConstructorCall(node);*/
            }

            private void checkIsConstructorCall(UCallExpression node) {
                if (!UastExpressionUtils.isConstructorCall(node)) {
                    return;
                }
                UReferenceExpression classRef = node.getClassReference();
                if (classRef != null) {
                    String className = UastUtils.getQualifiedName(classRef);
                    String uriValue = PACKAGE + ".common.DefaultUriRequest";
                    String pageValue = PACKAGE + ".common.DefaultPageUriRequest";

                    if (className.equals(uriValue) || className.equals(pageValue)) {
                        context.report(CALL_ISSUE, node, context.getLocation(node),
                                "ggtt ");
                    }
                }
            }

            private void checkIsMethod(UCallExpression node) {
                if (UastExpressionUtils.isMethodCall(node)) {
                    if (node.getReceiver() != null && node.getMethodName() != null) {
                        PsiMethod method = node.resolve();
                        if (context.getEvaluator().isMemberInClass(method, WM_ROUTER_CALL)) {
                            context.report(CALL_ISSUE, node, context.getLocation(node),
                                    "ffff");
                        }
                    }
                }
            }
        };
    }

}
