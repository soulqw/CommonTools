package com.qw.tools

import android.content.Context
import android.os.Handler
import android.os.Looper

class Container {
    companion object {
        @JvmStatic
        lateinit var context: Context
            private set

        @JvmStatic
        val handler: Handler = Handler(Looper.getMainLooper())

        @JvmStatic
        fun init(context: Context) {
            Container.context = context
        }
    }
}