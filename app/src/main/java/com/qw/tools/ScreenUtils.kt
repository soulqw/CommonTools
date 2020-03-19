package com.qw.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager

class ScreenUtils {
    companion object {
        const val SCREEN_BRIGHTNESS_AUTO_OFF = 0
        const val SCREEN_BRIGHTNESS_AUTO_ON = 1
        const val DEFAULT_BRIGHTNESS = (255 * 0.8).toInt()

        private var screenHeight = 0
        private var screenWidth = 0
        private var statusBarHeight = 0

        /**
         * 将dp转为pix
         *
         * @param cont
         * @return
         */
        @JvmStatic
        fun dip2px(cont: Context, dpValue: Float): Int {
            val scale = cont.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        /**
         * 转pix转化为dp
         *
         * @param context
         * @return
         */
        @JvmStatic
        fun px2dip(context: Context, pxValue: Int): Float {
            val scale = context.resources.displayMetrics.density
            return pxValue / scale + 0.5f
        }

        /**
         * 获取屏幕的宽度
         *
         * @param context
         * @return
         */
        private fun getScreenWidth(context: Context): Int {
            try {
                val wm = context.applicationContext
                        .getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val outMetrics = DisplayMetrics()
                wm.defaultDisplay.getMetrics(outMetrics)
                return outMetrics.widthPixels
            } catch (e: Exception) {
                //https://bugly.qq.com/v2/crash-reporting/crashes/9d9b1e7492/188104?pid=1
                //                Caused by:
                //                5 android.os.DeadSystemException:
                //                6 android.hardware.display.DisplayManagerGlobal.getDisplayInfo(DisplayManagerGlobal.java:135)
                //                7 android.view.Display.updateDisplayInfoLocked(Display.java:917)
                //                8 android.view.Display.getMetrics(Display.java:832)
                //                9 com.tomkey.commons.tools.r.a(ScreenUtils.java:67)
                //                10 com.dada.mobile.android.activity.welcome.ActivityNewWelcome.a(ActivityNewWelcome.java:182)
                //                11 com.dada.mobile.android.activity.welcome.ActivityNewWelcome.a(ActivityNewWelcome.java:159)
                //                12 com.dada.mobile.android.activity.welcome.k.a(NewWelcomePresenter.java:183)
                //                13 com.dada.mobile.android.activity.welcome.k.a(NewWelcomePresenter.java:177)
                //                14 com.dada.mobile.android.rxserver.b.onNext(BaseSubscriber2.java:112)
                return 0
            }

        }

        @JvmStatic
        fun getRealScreenWidth(context: Context): Int {
            if (screenWidth <= 0) {
                if (Build.VERSION.SDK_INT >= 17) {
                    try {
                        val wm = context.applicationContext
                                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
                        val point = Point()
                        wm.defaultDisplay.getRealSize(point)
                        screenWidth = point.x
                    } catch (e: Exception) {
                        screenWidth = getScreenWidth(context)
                    }

                } else {
                    screenWidth = getScreenWidth(context)
                }
            }
            return screenWidth
        }

        /**
         * 获取屏幕的高度
         *
         * @param context
         * @return
         */
        private fun getScreenHeight(context: Context): Int {
            val wm = context.applicationContext
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(outMetrics)
            return outMetrics.heightPixels
        }

        @JvmStatic
        fun getRealScreenHeight(context: Context): Int {
            if (screenHeight <= 0) {
                if (Build.VERSION.SDK_INT >= 17) {
                    try {
                        val wm = context.applicationContext
                                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
                        val point = Point()
                        wm.defaultDisplay.getRealSize(point)
                        screenHeight = point.y
                    } catch (e: Exception) {
                        screenHeight = getScreenHeight(context)
                    }

                } else {
                    screenHeight = getScreenHeight(context)
                }
            }
            return screenHeight
        }

        @JvmStatic
        fun getStatusBarHeight(context: Context): Int {
            if (statusBarHeight == 0) {
                try {
                    val interClazz = Class.forName("com.android.internal.R\$dimen")
                    val obj = interClazz.newInstance()
                    val field = interClazz.getField("status_bar_height")
                    val dpHeight = Integer.parseInt(field.get(obj).toString())
                    statusBarHeight = context.resources.getDimensionPixelSize(dpHeight)
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } catch (e: NoSuchFieldException) {
                    e.printStackTrace()
                } catch (e: InstantiationException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }

            return statusBarHeight
        }

        /**
         * 获取屏幕的亮度
         */
        @JvmStatic
        fun getScreenBrightness(activity: Activity): Int {
            var nowBrightnessValue = 0
            val resolver = activity.contentResolver
            try {
                nowBrightnessValue = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return nowBrightnessValue
        }

        /**
         * 设置屏幕亮度
         */
        @JvmStatic
        fun setBrightness(activity: Activity) {
            setBrightness(activity, DEFAULT_BRIGHTNESS)
        }

        @JvmStatic
        fun setBrightness(activity: Activity, brightness: Int) {
            var brightness = brightness
            if (brightness < 1)
                brightness = 1
            if (brightness > 255)
                brightness = 255

            val lp = activity.window.attributes
            lp.screenBrightness = brightness.toFloat() * (1f / 255f)
            activity.window.attributes = lp
        }

        @JvmStatic
        fun setAlpha(activity: Activity, alpha: Float) {
            var alpha = alpha
            val lp = activity.window.attributes
            if (alpha < 0f) {
                alpha = 0f
            }
            if (alpha > 1f) {
                alpha = 1f
            }
            lp.alpha = alpha
            activity.window.attributes = lp
        }

        /**
         * 停止自动亮度调节
         */
        @JvmStatic
        fun stopAutoBrightness(activity: Activity) {
            if (Build.VERSION.SDK_INT < 23 || Settings.System.canWrite(activity)) {
                Settings.System.putInt(activity.contentResolver,
                        Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)
            }
        }

        /**
         * 开启亮度自动调节
         */
        @JvmStatic
        fun startAutoBrightness(activity: Activity) {
            if (Build.VERSION.SDK_INT < 23 || Settings.System.canWrite(activity)) {
                Settings.System.putInt(activity.contentResolver,
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
            }
        }

        /**
         * 判断是否开启了自动亮度调节
         */
        @JvmStatic
        fun isAutoBrightness(context: Context): Boolean {
            var autoBrightness = false
            try {
                autoBrightness = Settings.System.getInt(context.contentResolver,
                        Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
            }

            return autoBrightness
        }

        /**
         * 需在oncreate调用
         */
        @SuppressLint("NewApi")
        @JvmStatic
        fun hideBottomUIMenu(context: Activity) {
            //隐藏虚拟按键，并且全屏
            if (Build.VERSION.SDK_INT < 19) { // lower api
                val v = context.window.decorView
                v.systemUiVisibility = View.GONE
            } else if (Build.VERSION.SDK_INT >= 19) {
                val _window = context.window
                val params = _window.attributes
                params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
                _window.attributes = params
            }
        }

        @SuppressLint("NewApi")
        @JvmStatic
        fun showBottomUIMenu(context: Activity) {
            //显示虚拟键盘
            if (Build.VERSION.SDK_INT < 19) {
                val v = context.window.decorView
                v.systemUiVisibility = View.VISIBLE
            } else if (Build.VERSION.SDK_INT >= 19) {
                val _window = context.window
                val params = _window.attributes
                params.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                _window.attributes = params
            }
        }
    }
}