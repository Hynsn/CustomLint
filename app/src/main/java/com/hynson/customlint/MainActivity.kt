package com.hynson.customlint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = "/test/activity")
class MainActivity : AppCompatActivity() {

    @BindView(R.id.btn_arouter)
    lateinit var btnRoute:Button

    @BindView(R.id.tv_title)
    lateinit var tvTitle:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this);
        btnRoute.setOnClickListener {
            Log.i("","ddd")
            tvTitle.text = "测试试用!"
            ARouter.getInstance().build("/test/activity").navigation()
        }

//        Log.i("","")
    }
}