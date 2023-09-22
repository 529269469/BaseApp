package com.example.psychology.base_app.utils.statusbar

import android.annotation.TargetApi
import android.app.AppOpsManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Binder
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.view.WindowManager
import java.io.Closeable
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.reflect.Method
import java.util.*
import java.util.regex.Pattern

/**
 * Author      : silveryTitan
 * Email       : silverytitan94@gmail.com
 * Date        : on 2021-11-16 09:49.
 * Description :
 */
object DeviceUtils {
    private val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private val KEY_FLYME_VERSION_NAME = "ro.build.display.id"
    private val FLYME = "flyme"
    private val ZTEC2016 = "zte c2016"
    private val ZUKZ1 = "zuk z1"
    private val ESSENTIAL = "essential"
    private val MEIZUBOARD = arrayOf("m9", "M9", "mx", "MX")
    private var sMiuiVersionName: String? = null
    private var sFlymeVersionName: String = ""
    private var sIsTabletChecked = false
    private var sIsTabletValue = false
    private val BRAND = Build.BRAND.lowercase()

    init {
        val properties = Properties()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // android 8.0，读取 /system/uild.prop 会报 permission denied
            var fileInputStream: FileInputStream? = null
            try {
                fileInputStream = FileInputStream(File(Environment.getRootDirectory(), "build.prop"))
                properties.load(fileInputStream)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                closeIOQuietly(fileInputStream)
            }
        }
        var clzSystemProperties: Class<*>? = null
        try {
            clzSystemProperties = Class.forName("android.os.SystemProperties")
            val getMethod = clzSystemProperties.getDeclaredMethod("get", String::class.java)
            // miui
            sMiuiVersionName = getLowerCaseName(properties, getMethod, KEY_MIUI_VERSION_NAME)
            //flyme
            sFlymeVersionName = getLowerCaseName(properties, getMethod, KEY_FLYME_VERSION_NAME)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun Context._isTablet(): Boolean =
        (resources.configuration.screenLayout or Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE

    fun Context.isTablet(): Boolean {
        if (sIsTabletChecked) {
            return sIsTabletValue
        }
        sIsTabletValue = _isTablet()
        sIsTabletChecked = true
        return sIsTabletValue
    }

    /** 判断是否是flyme系统 */
    fun isMeizu(): Boolean {
        return isPhone(MEIZUBOARD) || isFlyme()
    }

    fun isFlyme() =
        !TextUtils.isEmpty(sFlymeVersionName) && sFlymeVersionName.contains(FLYME)

    /**
     * 判断是否是MIUI系统
     */
    fun isMIUI(): Boolean = !TextUtils.isEmpty(sMiuiVersionName)


    fun isMIUIV5(): Boolean = "v5" == sMiuiVersionName


    fun isMIUIV6(): Boolean = "v6" == sMiuiVersionName


    fun isMIUIV7(): Boolean = "v7" == sMiuiVersionName


    fun isMIUIV8(): Boolean = "v8" == sMiuiVersionName


    fun isMIUIV9(): Boolean = "v9" == sMiuiVersionName

    fun isFlymeVersionHigher5_2_4(): Boolean {
        //查不到默认高于5.2.4
        var isHigher = true
        if (sFlymeVersionName != "") {
            val pattern = Pattern.compile("(\\d+\\.){2}\\d")
            val matcher = pattern.matcher(sFlymeVersionName)
            if (matcher.find()) {
                val versionString = matcher.group()
                if (versionString != null && versionString != "") {
                    val version = versionString.split("\\.").toTypedArray()
                    if (version.size == 3) {
                        if (Integer.valueOf(version[0]) < 5) {
                            isHigher = false
                        } else if (Integer.valueOf(version[0]) > 5) {
                            isHigher = true
                        } else {
                            if (Integer.valueOf(version[1]) < 2) {
                                isHigher = false
                            } else if (Integer.valueOf(version[1]) > 2) {
                                isHigher = true
                            } else {
                                if (Integer.valueOf(version[2]) < 4) {
                                    isHigher = false
                                } else if (Integer.valueOf(version[2]) >= 5) {
                                    isHigher = true
                                }
                            }
                        }
                    }
                }
            }
        }
        return isMeizu() && isHigher
    }

    /**
     * 判断是否为小米
     * https://dev.mi.com/doc/?p=254
     */
    fun isXiaomi(): Boolean = Build.MANUFACTURER.lowercase() == "xiaomi"


    fun isVivo(): Boolean = BRAND.contains("vivo") || BRAND.contains("bbk")


    fun isOppo(): Boolean = BRAND.contains("oppo")


    fun isHuawei(): Boolean = BRAND.contains("huawei") || BRAND.contains("honor")


    fun isEssentialPhone(): Boolean = BRAND.contains("essential")


    /**
     * 判断是否为 ZUK Z1 和 ZTK C2016。
     * 两台设备的系统虽然为 android 6.0，但不支持状态栏icon颜色改变，因此经常需要对它们进行额外判断。
     */
    fun isZUKZ1(): Boolean = Build.MODEL != null && Build.MODEL.lowercase().contains(ZUKZ1)


    fun isZTKC2016(): Boolean = Build.MODEL != null && Build.MODEL.lowercase().contains(ZTEC2016)


    private fun isPhone(boards: Array<String>): Boolean {
        val board = Build.BOARD ?: return false
        for (board1 in boards) {
            if (board == board1) {
                return true
            }
        }
        return false
    }

    /**
     * 判断悬浮窗权限（目前主要用户魅族与小米的检测）。
     */
    fun isFloatWindowOpAllowed(context: Context): Boolean {
        val version = Build.VERSION.SDK_INT
        return if (version >= 19) {
            checkOp(context, 24) // 24 是AppOpsManager.OP_SYSTEM_ALERT_WINDOW 的值，该值无法直接访问
        } else {
            try {
                context.applicationInfo.flags and 1 shl 27 == 1 shl 27
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    @TargetApi(19)
    private fun checkOp(context: Context, op: Int): Boolean {
        val version = Build.VERSION.SDK_INT
        if (version >= Build.VERSION_CODES.KITKAT) {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                val method = manager.javaClass.getDeclaredMethod("checkOp", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, String::class.java)
                val property = method.invoke(
                    manager, op,
                    Binder.getCallingUid(), context.packageName
                ) as Int
                return AppOpsManager.MODE_ALLOWED == property
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    private fun getLowerCaseName(p: Properties, get: Method, key: String): String {
        var name = p.getProperty(key)
        if (name == null) {
            try {
                name = get.invoke(null, key) as String
            } catch (ignored: java.lang.Exception) {
            }
        }
        if (name != null) {
            name = name.lowercase()
        }
        return name
    }

    /** 获取屏幕尺寸 */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    fun getScreenSize(context: Context): Point {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point(display.width, display.height)
        } else {
            val point = Point()
            display.getSize(point)
            point
        }
    }

    /**
     * 安静关闭 IO
     *
     * @param closeables closeables
     */
    fun closeIOQuietly(vararg closeables: Closeable?) {
        if (closeables == null) return
        for (closeable in closeables) {
            if (closeable != null) {
                try {
                    closeable.close()
                } catch (ignored: IOException) {
                }
            }
        }
    }
}