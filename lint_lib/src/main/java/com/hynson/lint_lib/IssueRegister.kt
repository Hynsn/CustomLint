package com.hynson.lint_lib

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue

class IssueRegister : IssueRegistry() {
    override val issues: List<Issue>
        get() = arrayListOf(LogDetector.ISSUE)
    override val api: Int = com.android.tools.lint.detector.api.CURRENT_API
    override val minApi: Int
        get() = 1
}