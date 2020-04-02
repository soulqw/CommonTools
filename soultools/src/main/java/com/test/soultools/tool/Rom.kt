package com.qw.tools

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class Rom {
    companion object {
        const val ROM_MIUI = "MIUI"
        const val ROM_EMUI = "EMUI"
        const val ROM_FLYME = "FLYME"
        const val ROM_OPPO = "OPPO"
        const val ROM_SMARTISAN = "SMARTISAN"
        const val ROM_VIVO = "VIVO"
        const val ROM_QIKU = "QIKU"

        /**
         * 去通用设置页
         */
        const val ACTION_DEFAULT = 0
        /**
         * 去白名单
         */
        const val ACTION_WHITE = 1
        /**
         * 去系统自启动
         */
        const val ACTION_AUTO_START = 2
        /**
         * 去系统权限
         */
        const val ACTION_PERMISSION = 3
        /**
         * 去通知设置
         */
        const val ACTION_NOTIFICATION = 4

        private const val KEY_VERSION_MIUI = "ro.miui.ui.version.name"
        private const val KEY_VERSION_EMUI = "ro.build.version.emui"
        private const val KEY_VERSION_OPPO = "ro.build.version.opporom"
        private const val KEY_VERSION_SMARTISAN = "ro.smartisan.version"
        private const val KEY_VERSION_VIVO = "ro.vivo.os.version"

        private const val TAG = "Rom"

        private var sName: String? = null
        private var sVersion: String? = null

        @JvmStatic
        fun goDadaKnightSetting(context: Context, action: Int) {
            goSetting(context, "com.dada.mobile.android", "达达骑士", action)
        }

        @JvmStatic
        fun goSetting(context: Context, packageName: String?, label: String?, action: Int) {
            try {
                context.startActivity(getSettingIntent(packageName, label, action))
            } catch (e: Exception) {
                e.printStackTrace()
                context.startActivity(Intent(Settings.ACTION_SETTINGS))
            }
        }

        @JvmStatic
        fun getRomName(): String {
            var brand = Build.BRAND.toLowerCase()
            //特殊标识特殊处理
            if (brand == "honor") {
                brand = "huawei"
            }
            return brand.toLowerCase()
        }

        private fun getSettingIntent(packageName: String?, label: String?, action: Int): Intent {
            var componentName: ComponentName? = null
            val brand = getRomName()
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            when (brand) {
                "xiaomi" -> {
                    componentName = getXiaoMiComponentName(action)
                    intent.putExtra("package_name", packageName)
                    intent.putExtra("package_label", label)
                }
                "huawei" -> componentName = getHuaWeiComponentName(action)
                "vivo" -> componentName = getVivoComponentName(action)
                "oppo" -> componentName = getOppoComponentName(action)
                "meizu" -> componentName = getMeiZuComponentName(action)
                "360" -> componentName = ComponentName("com.yulong.android.coolsafe",
                        "com.yulong.android.coolsafe.ui.activity.autorun.AutoRunListActivity")
                "oneplus" -> componentName = ComponentName("com.oneplus.security",
                        "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActivity")
                "samsung" -> componentName = ComponentName("com.samsung.android.sm",
                        "com.samsung.android.sm.app.dashboard.SmartManagerDashBoardActivity")
                else -> {
                }
            }
            if (componentName != null) {
                intent.component = componentName
            } else {
                intent.action = Settings.ACTION_SETTINGS
            }
            return intent
        }

        private fun getOppoComponentName(action: Int): ComponentName? {
            val oppoOsVersion = getVersionFloat(3.2f)
            val componentName: ComponentName?
            if (oppoOsVersion >= 3.2f) {
                when (action) {
                    ACTION_WHITE, ACTION_AUTO_START, ACTION_PERMISSION -> componentName = ComponentName("com.coloros.safecenter",
                            "com.coloros.privacypermissionsentry.PermissionTopActivity")
                    else -> componentName = null
                }
            } else {
                componentName = getOppoOsOsOld(action)
            }
            return componentName
        }

        private fun getOppoOsOsOld(action: Int): ComponentName {
            return when (action) {
                ACTION_WHITE -> ComponentName("com.coloros.oppoguardelf",
                        "com.coloros.powermanager.fuelgaue.PowerUsageModelActivity")
                ACTION_AUTO_START -> ComponentName("com.coloros.safecenter",
                        "com.coloros.safecenter.startupapp.StartupAppListActivity")
                ACTION_NOTIFICATION -> ComponentName("com.android.settings",
                        "com.android.settings.SubSettings")
                else -> ComponentName("com.coloros.safecenter",
                        "com.coloros.safecenter.MainActivity")
            }
        }

        /**
         * 适配vivo的厂商跳转
         */
        private fun getVivoComponentName(action: Int): ComponentName? {
            val vivoOsVersion = getVersionFloat(4.0f)
            val componentName: ComponentName?
            if (vivoOsVersion >= 4.0f) {
                when (action) {
                    ACTION_AUTO_START -> componentName = ComponentName("com.iqoo.secure",
                            "com.iqoo.secure.MainGuideActivity")
                    ACTION_WHITE -> componentName = ComponentName("com.iqoo.powersaving",
                            "com.iqoo.powersaving.PowerSavingManagerActivity")
                    else -> componentName = null
                }
            } else {
                componentName = getVivoOsOld(action)
            }
            return componentName
        }

        private fun getVivoOsOld(action: Int): ComponentName {
            return when (action) {
                ACTION_AUTO_START -> ComponentName("com.iqoo.secure",
                        "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")
                ACTION_WHITE -> ComponentName("com.vivo.abe",
                        "com.vivo.applicationbehaviorengine.ui.ExcessivePowerManagerActivity")
                ACTION_NOTIFICATION -> ComponentName("com.android.systemui",
                        "com.android.systemui.vivo.common.notification.settings.StatusbarSettingActivity")
                ACTION_PERMISSION -> ComponentName("com.iqoo.secure",
                        "com.iqoo.secure.MainActivity")
                else -> ComponentName("com.iqoo.secure", "com.iqoo.secure.MainActivity")
            }
        }

        /**
         * 适配华为的厂商跳转
         */
        private fun getHuaWeiComponentName(action: Int): ComponentName {
            return when (action) {
                ACTION_AUTO_START, ACTION_WHITE -> ComponentName("com.huawei.systemmanager",
                        "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")
                ACTION_NOTIFICATION -> ComponentName("com.huawei.systemmanager",
                        "com.huawei.notificationmanager.ui.NotificationManagmentActivity")
                ACTION_PERMISSION -> ComponentName("com.huawei.systemmanager",
                        "com.huawei.permissionmanager.ui.MainActivity")
                else -> ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity")
            }
        }

        /**
         * 适配小米
         */
        private fun getXiaoMiComponentName(action: Int): ComponentName {
            return when (action) {
                ACTION_AUTO_START -> ComponentName("com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity")
                ACTION_WHITE -> ComponentName("com.miui.powerkeeper",
                        "com.miui.powerkeeper.ui.HiddenAppsConfigActivity")
                ACTION_NOTIFICATION -> ComponentName("com.android.settings",
                        "com.android.settings.SubSettings")
                ACTION_PERMISSION -> ComponentName("com.miui.securitycenter",
                        "com.miui.permcenter.MainAcitivty")
                else -> ComponentName("com.miui.securitycenter", "com.miui.permcenter.MainAcitivty")
            }
        }

        /**
         * 适配魅族
         */
        private fun getMeiZuComponentName(action: Int): ComponentName {
            return when (action) {
                ACTION_AUTO_START, ACTION_WHITE -> ComponentName("com.meizu.safe",
                        "com.meizu.safe.permission.SmartBGActivity")
                else -> ComponentName("com.meizu.safe",
                        "com.meizu.safe.permission.AppPermissionActivity")
            }
        }

        @JvmStatic
        fun isEmui(): Boolean {
            return check(ROM_EMUI)
        }

        @JvmStatic
        fun isMiui(): Boolean {
            return check(ROM_MIUI)
        }

        @JvmStatic
        fun isVivo(): Boolean {
            return check(ROM_VIVO)
        }

        @JvmStatic
        fun isOppo(): Boolean {
            return check(ROM_OPPO)
        }

        @JvmStatic
        fun isFlyme(): Boolean {
            return check(ROM_FLYME)
        }

        @JvmStatic
        fun is360(): Boolean {
            return check(ROM_QIKU) || check("360")
        }

        @JvmStatic
        fun isSmartisan(): Boolean {
            return check(ROM_SMARTISAN)
        }

        @JvmStatic
        fun getVersion(): String? {
            if (sVersion == null) {
                check("")
            }
            return sVersion
        }

        /**
         * 有些厂商会获取 类似v3.2这种，需要过滤掉字母和空格 ->3.2
         * flayme 6.2.02 -> 6.202
         */
        @JvmStatic
        fun getVersionFloat(defaultValue: Float): Float {
            var str = getVersion()
            try {
                //先去掉字母和空格
                str = str!!.replace("[A-Za-z ]".toRegex(), "")
                val charIndex = str.indexOf(".")
                //如果有小数点，只留一个拼接
                if (charIndex > 0) {
                    str = str.replace(".", "")
                    val newStringStart = str.substring(0, charIndex)
                    val newStringEnd = str.substring(charIndex, str.length)
                    str = "$newStringStart.$newStringEnd"
                }
                return java.lang.Float.parseFloat(str)
            } catch (e: Exception) {
                return defaultValue
            }

        }

        private fun check(rom: String): Boolean {
            if (sName != null) {
                return sName == rom
            }
            sVersion = getProp(KEY_VERSION_MIUI)
            if(!TextUtils.isEmpty(sVersion)) {
                sName = ROM_MIUI
                return sName == rom
            }
            sVersion = getProp(KEY_VERSION_EMUI)
            if(!TextUtils.isEmpty(sVersion)) {
                sName = ROM_EMUI
                return sName == rom
            }
            sVersion = getProp(KEY_VERSION_OPPO)
            if(!TextUtils.isEmpty(sVersion)) {
                sName = ROM_OPPO
                return sName == rom
            }
            sVersion = getProp(KEY_VERSION_VIVO)
            if(!TextUtils.isEmpty(sVersion)) {
                sName = ROM_VIVO
                return sName == rom
            }
            sVersion = getProp(KEY_VERSION_SMARTISAN)
            if(!TextUtils.isEmpty(sVersion)) {
                sName = ROM_SMARTISAN
                return sName == rom
            }

            sVersion = Build.DISPLAY
            if (sVersion!!.toUpperCase().contains(ROM_FLYME)) {
                sName = ROM_FLYME
            } else {
                sVersion = Build.UNKNOWN
                sName = Build.MANUFACTURER.toUpperCase()
            }
            return sName == rom
        }

        private fun getProp(name: String): String? {
            val line: String
            var input: BufferedReader? = null
            try {
                val p = Runtime.getRuntime().exec("getprop $name")
                input = BufferedReader(InputStreamReader(p.inputStream), 1024)
                line = input.readLine()
                input.close()
            } catch (ex: IOException) {
                Log.e(TAG, "Unable to read prop $name", ex)
                return null
            } finally {
                if (input != null) {
                    try {
                        input.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
            return line
        }
    }
}