package com.qw

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.qw.tools.R
import com.test.soultools.tool.log.TLog
import com.test.soultools.tool.log.TLog.d
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Author: 思忆
 * Date: Created in 2020/5/14 4:24 PM
 */
class ActivityMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.btn2).setOnClickListener { startActivity(Intent(this@ActivityMain, ActivitySecond::class.java)) }
        findViewById<View>(R.id.btn1)?.setOnClickListener {
            startActivity(Intent(this@ActivityMain, ActivityThree::class.java))
        }

    }


}