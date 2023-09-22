package com.example.psychology.base_app.utils.statusbar

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Build.VERSION_CODES.KITKAT
import android.view.*
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import com.example.psychology.base_app.utils.statusbar.DeviceUtils.isEssentialPhone
import com.example.psychology.base_app.utils.statusbar.DeviceUtils.isMIUIV5
import com.example.psychology.base_app.utils.statusbar.DeviceUtils.isMIUIV6
import com.example.psychology.base_app.utils.statusbar.DeviceUtils.isMIUIV7
import com.example.psychology.base_app.utils.statusbar.DeviceUtils.isMIUIV8
import com.example.psychology.base_app.utils.statusbar.DeviceUtils.isMIUIV9
import com.example.psychology.base_app.utils.statusbar.DeviceUtils.isMeizu
import com.example.psychology.base_app.utils.statusbar.DeviceUtils.isZTKC2016
import com.example.psychology.base_app.utils.statusbar.DeviceUtils.isZUKZ1
import com.example.psychology.base_app.utils.statusbar.StatusBarType.*
import java.lang.reflect.Field

/**
 * Author      : silveryTitan
 * Email       : silverytitan94@gmail.com
 * Date        : on 2021-09-01 15:18.
 * Description : 沉浸式状态栏
 */
object StatusBar {

    private const val STATUS_BAR_DEFAULT_HEIGHT_DP = 25 // 大部分状态栏都是25dp

    // 在某些机子上存在不同的density值，所以增加两个虚拟值
    var sVirtualDensity = -1f
    var sVirtualDensityDpi = -1f
    private var sStatusbarHeight = -1

    @StatusBarType
    var mStatuBarType = STATUSBAR_TYPE_DEFAULT
    private var sTransparentValue: Int? = null


    fun Activity.translucent() {
        window.translucent()
    }

    fun Window.translucent() {
        translucent(this, 0x40000000)
    }

    fun supportTranslucent() =
        Build.VERSION.SDK_INT >= KITKAT && !(isEssentialPhone() && Build.VERSION.SDK_INT < 26)

    /**
     * 设置沉浸式状态栏样式
     *
     * @param isDark    是否是深色的状态栏
     * @param colorOn5x 颜色
     */
    fun Activity.initStatusBarStyle(isDark: Boolean, @ColorInt colorOn5x: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }
        //设置沉浸式状态栏的颜色
        translucent(this, colorOn5x)
        //修改状态栏的字体颜色
        if (isDark) setStatusBarDarkMode(this)
        else setStatusBarLightMode(this)
    }

    /**
     * 沉浸式状态栏。
     * 支持 4.4 以上版本的 MIUI 和 Flyme，以及 5.0 以上版本的其他 Android。
     *
     * @param activity 需要被设置沉浸式状态栏的 Activity。
     */
    fun translucent(activity: Activity, @ColorInt colorOn5x: Int) {
        translucent(activity.window, colorOn5x)
    }

    @TargetApi(KITKAT)
    fun translucent(window: Window, @ColorInt colorOn5x: Int) {
        if (!supportTranslucent()) {
            // 版本小于4.4，绝对不考虑沉浸式
            return
        }
        if (isNotchOfficialSupport()) {
            handleDisplayCutoutMode(window)
        }

        // 小米和魅族4.4 以上版本支持沉浸式
        // 小米 Android 6.0 ，开发版 7.7.13 及以后版本设置黑色字体又需要 clear FLAG_TRANSLUCENT_STATUS, 因此还原为官方模式
        //设置下面判断会将魅族状态栏设置为透明并顶上去，测试其他手机状态是否正常后在考虑是否需要这个判断
        /*if (isMeizu() || (isMIUI() && Build.VERSION.SDK_INT < Build.VERSION_CODES.M)) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            return
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && supportTransclentStatusBar6()) {
                // android 6以后可以改状态栏字体颜色，因此可以自行设置为透明
                // ZUK Z1是个另类，自家应用可以实现字体颜色变色，但没开放接口
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = colorOn5x
            } else {
                // android 5不能修改状态栏字体颜色，因此直接用FLAG_TRANSLUCENT_STATUS，nexus表现为半透明
                // 魅族和小米的表现如何？
                // update: 部分手机运用FLAG_TRANSLUCENT_STATUS时背景不是半透明而是没有背景了。。。。。
//                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                // 采取setStatusBarColor的方式，部分机型不支持，那就纯黑了，保证状态栏图标可见
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = colorOn5x
            }
        }
    }

    fun isNotchOfficialSupport() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

    @TargetApi(Build.VERSION_CODES.P)
    private fun handleDisplayCutoutMode(window: Window) {
        val decorView = window.decorView
        if (ViewCompat.isAttachedToWindow(decorView)) {
            realHandleDisplayCutoutMode(window, decorView)
        } else {
            decorView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    v.removeOnAttachStateChangeListener(this)
                    realHandleDisplayCutoutMode(window, v)
                }

                override fun onViewDetachedFromWindow(v: View) {}
            })
        }
    }

    @TargetApi(Build.VERSION_CODES.P)
    private fun realHandleDisplayCutoutMode(window: Window, decorView: View) {
        if (decorView.rootWindowInsets != null && decorView.rootWindowInsets.displayCutout != null) {
            val params = window.attributes
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = params
        }
    }

    /**
     * 设置状态栏黑色字体图标，
     * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
     *
     * @param activity 需要被处理的 Activity
     */
    fun setStatusBarLightMode(activity: Activity?): Boolean {
        if (activity == null) return false
        // 无语系列：ZTK C2016只能时间和电池图标变色。。。。
        if (isZTKC2016()) {
            return false
        }
        if (mStatuBarType != STATUSBAR_TYPE_DEFAULT) {
            return setStatusBarLightMode(activity, mStatuBarType)
        }
        if (Build.VERSION.SDK_INT >= KITKAT) {
            if (isMIUICustomStatusBarLightModeImpl() && MIUISetStatusBarLightMode(activity.window, true)) {
                mStatuBarType = STATUSBAR_TYPE_MIUI
                return true
            } else if (FlymeSetStatusBarLightMode(activity.window, true)) {
                mStatuBarType = STATUSBAR_TYPE_FLYME
                return true
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Android6SetStatusBarLightMode(activity.window, true)
                mStatuBarType = STATUSBAR_TYPE_ANDROID6
                return true
            }
        }
        return false
    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
     *
     * @param activity 需要被处理的 Activity
     * @param type     StatusBar 类型，对应不同的系统
     */
    private fun setStatusBarLightMode(activity: Activity, @StatusBarType type: Int): Boolean {
        return when (type) {
            STATUSBAR_TYPE_MIUI -> {
                MIUISetStatusBarLightMode(activity.window, true)
            }
            STATUSBAR_TYPE_FLYME -> {
                FlymeSetStatusBarLightMode(activity.window, true)
            }
            STATUSBAR_TYPE_ANDROID6 -> {
                Android6SetStatusBarLightMode(activity.window, true)
            }
            else -> false
        }
    }

    /**
     * 设置状态栏白色字体图标
     * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
     */
    fun setStatusBarDarkMode(activity: Activity?): Boolean {
        if (activity == null) return false
        if (mStatuBarType == STATUSBAR_TYPE_DEFAULT) {
            // 默认状态，不需要处理
            return true
        }
        return when (mStatuBarType) {
            STATUSBAR_TYPE_MIUI -> {
                MIUISetStatusBarLightMode(activity.window, false)
            }
            STATUSBAR_TYPE_FLYME -> {
                FlymeSetStatusBarLightMode(activity.window, false)
            }
            STATUSBAR_TYPE_ANDROID6 -> {
                Android6SetStatusBarLightMode(activity.window, false)
            }
            else -> true
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun changeStatusBarModeRetainFlag(window: Window, out: Int): Int {
        var out1 = out
        out1 = retainSystemUiFlag(window, out1, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        out1 = retainSystemUiFlag(window, out1, View.SYSTEM_UI_FLAG_FULLSCREEN)
        out1 = retainSystemUiFlag(window, out1, View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        out1 = retainSystemUiFlag(window, out1, View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        out1 = retainSystemUiFlag(window, out1, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        out1 = retainSystemUiFlag(window, out1, View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        return out
    }

    fun retainSystemUiFlag(window: Window, out: Int, type: Int): Int {
        var out1 = out
        val now = window.decorView.systemUiVisibility
        if (now and type == type) {
            out1 = out1 or type
        }
        return out1
    }

    /**
     * 设置状态栏字体图标为深色，Android 6
     *
     * @param window 需要设置的窗口
     * @param light  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    @TargetApi(Build.VERSION_CODES.M)
    private fun Android6SetStatusBarLightMode(window: Window, light: Boolean): Boolean {
        var systemUi = if (light) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        systemUi = changeStatusBarModeRetainFlag(window, systemUi)
        window.decorView.systemUiVisibility = systemUi
        if (isMIUIV9()) {
            // MIUI 9 低于 6.0 版本依旧只能回退到以前的方案
            // https://github.com/Tencent/QMUI_Android/issues/160
            MIUISetStatusBarLightMode(window, light)
        }
        return true
    }

    /**
     * 设置状态栏字体图标为深色，需要 MIUIV6 以上
     *
     * @param window 需要设置的窗口
     * @param light  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回 true
     */
    fun MIUISetStatusBarLightMode(window: Window?, light: Boolean): Boolean {
        var result = false
        if (window != null) {
            val clazz: Class<*> = window.javaClass
            try {
                val darkModeFlag: Int
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                if (light) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag) //状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag) //清除黑色字体
                }
                result = true
            } catch (ignored: Exception) {
            }
        }
        return result
    }

    /**
     * 更改状态栏图标、文字颜色的方案是否是MIUI自家的， MIUI9 && Android 6 之后用回Android原生实现
     * 见小米开发文档说明：https://dev.mi.com/console/doc/detail?pId=1159
     */
    private fun isMIUICustomStatusBarLightModeImpl() =
        if (isMIUIV9() && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) true
        else isMIUIV5() || isMIUIV6() || isMIUIV7() || isMIUIV8()

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为 Flyme 用户
     *
     * @param window 需要设置的窗口
     * @param light  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    fun FlymeSetStatusBarLightMode(window: Window?, light: Boolean): Boolean {
        var result = false
        if (window != null) {
            // flyme 在 6.2.0.0A 支持了 Android 官方的实现方案，旧的方案失效
            Android6SetStatusBarLightMode(window, light)
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                value = if (light) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (ignored: java.lang.Exception) {
            }
        }
        return result
    }

    /**
     * 获取是否全屏
     *
     * @return 是否全屏
     */
    fun isFullScreen(activity: Activity): Boolean {
        var ret = false
        try {
            val attrs = activity.window.attributes
            ret = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN != 0
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ret
    }

  
    /**
     * 检测 Android 6.0 是否可以启用 window.setStatusBarColor(Color.TRANSPARENT)。
     */
    fun supportTransclentStatusBar6() = !(isZUKZ1() || isZTKC2016())

    /**
     * 获取状态栏的高度。
     */
    fun getStatusBarHeight(context: Context): Int {
        if (sStatusbarHeight == -1) {
            initStatusBarHeight(context)
        }
        return sStatusbarHeight
    }

    private fun initStatusBarHeight(context: Context) {
        val clazz: Class<*>
        var obj: Any? = null
        var field: Field? = null
        try {
            clazz = Class.forName("com.android.internal.R\$dimen")
            obj = clazz.newInstance()
            if (isMeizu()) {
                try {
                    field = clazz.getField("status_bar_height_large")
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
            if (field == null) {
                field = clazz.getField("status_bar_height")
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        if (field != null && obj != null) {
            try {
                val id = field[obj].toString().toInt()
                sStatusbarHeight = context.resources.getDimensionPixelSize(id)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }

    }

    fun setVirtualDensity(density: Float) {
        sVirtualDensity = density
    }

    fun setVirtualDensityDpi(densityDpi: Float) {
        sVirtualDensityDpi = densityDpi
    }

    /** 全屏 */
    fun Activity.fullScreen() {
        window.fullScreen()
    }

    /** 全屏 */
    fun Window.fullScreen() {
        if (Build.VERSION.SDK_INT >= KITKAT) {
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }

    /**
     * 取消全屏
     *
     * @param activity
     * @param statusBarColor     状态栏的颜色
     * @param navigationBarColor 导航栏的颜色
     */
    fun cancelFullScreen(activity: Activity, @ColorInt statusBarColor: Int, @ColorInt navigationBarColor: Int) {
        cancelFullScreen(activity.window, statusBarColor, navigationBarColor)
    }

    /**
     * 取消全屏
     *
     * @param window
     * @param statusBarColor     状态栏的颜色
     * @param navigationBarColor 导航栏的颜色
     */
    fun cancelFullScreen(window: Window, @ColorInt statusBarColor: Int, @ColorInt navigationBarColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            )
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (statusBarColor != -1) {
                window.statusBarColor = statusBarColor
            }
            if (navigationBarColor != -1) {
                window.navigationBarColor = navigationBarColor
            }
        }
    }

    /** 取消全屏 */
    fun Activity.cancelFullScreen() {
        window.cancelFullScreen()
    }

    /** 取消全屏 */
    fun Window.cancelFullScreen() {
        cancelFullScreen(this, -1, -1)
    }


    fun setNavigationBarColor(activity: Activity, color: Int) {
        when {
            Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP -> {
                //5.0以上可以直接设置 navigation颜色
                activity.window.navigationBarColor = color
            }
            Build.VERSION.SDK_INT >= KITKAT -> {
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                val decorView = activity.window.decorView as ViewGroup
                val navigationBar = View(activity)
                val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getNavigationBarHeight(activity))
                params.gravity = Gravity.BOTTOM
                navigationBar.layoutParams = params
                navigationBar.setBackgroundColor(color)
                decorView.addView(navigationBar)
            }
            else -> {
                //4.4以下无法设置NavigationBar颜色
            }
        }
    }

    fun getNavigationBarHeight(context: Context): Int {
        var height = 0
        val id = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (id > 0) {
            height = context.resources.getDimensionPixelSize(id)
        }
        return height
    }
}