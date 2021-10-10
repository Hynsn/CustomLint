package com.hynson.customlint

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (isDebug()) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }

    private fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }
}