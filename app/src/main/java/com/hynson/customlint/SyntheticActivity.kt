package com.hynson.customlint

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_synthetic.*
import java.text.SimpleDateFormat
import java.util.*

@Route(path = "/test/activity")
class SyntheticActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_synthetic)

        Log.i(TAG,testTV.javaClass.name)

        testTV.text = time(this,"at")
    }
    companion object {
        var TAG: String = SyntheticActivity::class.java.simpleName
    }

    @SuppressLint("SimpleDateFormat")
    fun time(context: Context, at:String):String{
        val calendar = Calendar.getInstance()
        val timeZone: TimeZone = TimeZone.getTimeZone("tz")
        val format = SimpleDateFormat("MMM dd,yyyy " + at + " HH:mm:ss")
        format.timeZone = timeZone
        return format.format(calendar.time)
    }
}