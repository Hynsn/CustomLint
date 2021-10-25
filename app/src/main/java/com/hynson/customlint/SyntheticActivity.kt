package com.hynson.customlint

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_synthetic.*
import kotlinx.android.parcel.Parcelize

@Route(path = "/test/activity")
class SyntheticActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_synthetic)

        Log.i(TAG,testTV.javaClass.name)

        testTV.text = "测试使用"
    }
    companion object {
        var TAG: String = SyntheticActivity::class.java.simpleName
    }
}