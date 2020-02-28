package com.qw

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.qw.tools.storage.SharedPreferencesHelper

/**
 *
 * @author cd5160866
 * @date 2020/2/28
 */
class ActivityMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        View(this).also {
            it.setBackgroundColor(Color.WHITE)
            setContentView(it)
        }
        SharedPreferencesHelper.init(applicationContext)

        val x = SharedPreferencesHelper.MY_NUMBER

        SharedPreferencesHelper.MY_NUMBER = 20

        Toast.makeText(applicationContext, "value:${SharedPreferencesHelper.MY_NUMBER}", Toast.LENGTH_SHORT).show()
    }

}