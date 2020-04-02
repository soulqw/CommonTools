package com.qw.tools

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import java.lang.reflect.Field

class Toasts {


    companion object {
        private var sField_TN: Field? = null
        private var sField_TN_Handler: Field? = null

        init {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                try {
                    //TN.class
                    sField_TN = Toast::class.java.getDeclaredField("mTN")
                    sField_TN!!.isAccessible = true

                    //Handler.class
                    sField_TN_Handler = sField_TN!!.type.getDeclaredField("mHandler")
                    sField_TN_Handler!!.isAccessible = true
                } catch (ignored: Exception) {
                }

            }
        }

        private fun hock(toast: Toast) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return
            }
            try {
                val tn = sField_TN!!.get(toast)
                val preHandler = sField_TN_Handler!!.get(tn) as Handler
                sField_TN_Handler!!.set(tn, SafelyHandlerWrapper(preHandler))
            } catch (ignored: Exception) {
            }
        }

        private fun shortToast(context: Context, msg: String?) {
            try {
                var msg = msg
                if (!TextUtils.isEmpty(msg) && msg!!.length > 30) {
                    msg = msg.substring(0, 30)
                }
                val mToast = Toast(context)
                val toastRoot = View.inflate(context, R.layout.toast_message, null)
                mToast.view = toastRoot

                //发面在mz 6.0 上会有少量的崩溃
                mToast.duration = Toast.LENGTH_SHORT
                val root = mToast.view ?: return
                val messageTv = root.findViewById(R.id.message_tv) as TextView
                messageTv.text = msg
                hock(mToast)
                mToast.show()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        @JvmStatic
        fun shortToast(msgId: Int) {
            if (msgId > 0) {
                shortToast(Container.context.getString(msgId))
            }
        }

        @JvmStatic
        fun shortToast(msg: String?) {
            shortToast(Container.context, msg)
        }

        private fun longToast(context: Context, msg: String?) {
            try {
                var msg = msg
                if (!TextUtils.isEmpty(msg) && msg!!.length > 50) {
                    msg = msg.substring(0, 50)
                }
                val mToast = Toast(context)
                val toastRoot = View.inflate(context, R.layout.toast_message, null)
                mToast.view = toastRoot
                mToast.duration = Toast.LENGTH_LONG
                val root = mToast.view ?: return
                val messageTv = root.findViewById(R.id.message_tv) as TextView
                messageTv.text = msg
                hock(mToast)
                mToast.show()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        @JvmStatic
        fun longToast(msgId: Int) {
            if (msgId > 0) {
                longToast(Container.context.getString(msgId))
            }
        }

        @JvmStatic
        fun longToast(msg: String?) {
            longToast(Container.context, msg)
        }
    }

    class SafelyHandlerWrapper(private val impl: Handler) : Handler() {

        override fun dispatchMessage(msg: Message) {
            try {
                super.dispatchMessage(msg)
            } catch (ignored: Exception) {
            }
        }

        override fun handleMessage(msg: Message) {
            //委托给原有的 Handler 执行
            impl.handleMessage(msg)
        }
    }
}