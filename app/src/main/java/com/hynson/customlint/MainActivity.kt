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
import rx.Observable
import rx.Subscription
import rx.functions.Action1

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
            tvTitle.text = resources.getString(R.string.hello_world)
            ARouter.getInstance().build("/test/activity").navigation()
        }

        Log.i("","")

        callRxJava()
    }

    fun callRxJava(){
        val s:Subscription = Observable.just("Test")
            .subscribe(Action1 {
                Log.i("1","2")
            })
    }
}