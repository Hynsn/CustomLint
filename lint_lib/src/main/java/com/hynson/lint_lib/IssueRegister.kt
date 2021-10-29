package com.hynson.lint_lib

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue

class IssueRegister : IssueRegistry() {
    override val issues: List<Issue>
        get() = arrayListOf(
            SubscribeDetector.ISSUE,
            AnnotationDetector.ISSUE1,
            AnnotationDetector.ISSUE2,
            //ARouterDetector.ISSUE,
            LogDetector.ISSUE,
            ImageResourceDetector.ISSUE1)
    override val api: Int = com.android.tools.lint.detector.api.CURRENT_API
    override val minApi: Int
        get() = 1
}