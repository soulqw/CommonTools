package com.qw

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.qw.tools.R
import com.test.soultools.tool.log.TLog

class ActivitySecond:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        findViewById<View>(R.id.btn1).setOnClickListener {
            TLog.d("qw", "This is An Log in kt:")
        }
    }
}