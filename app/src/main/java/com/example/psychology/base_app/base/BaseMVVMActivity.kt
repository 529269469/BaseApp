package com.example.psychology.base_app.base

import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.example.psychology.base_app.utils.KeyboardUtils
import com.example.psychology.base_app.utils.base.clickutils.ClickUtils
import com.example.psychology.base_app.utils.base.vmutils.ClassUtil
import com.example.psychology.base_app.utils.statusbar.StatusBar.initStatusBarStyle

abstract class BaseMVVMActivity<VB : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes var layoutId: Int,
    val statusBarColor: String? = "#FFFFFF"
) :
    AppCompatActivity(), IBaseView, View.OnClickListener {
    /** 控件绑定 */
    val bind: VB by binding(layoutId)
    private fun <T : ViewDataBinding> binding(@LayoutRes resId: Int): Lazy<T> = lazy {
        DataBindingUtil.setContentView(this, resId)
    }

    /** viewModel */
    private var _viewModel: VM? = null
    val viewModel: VM
        get() = _viewModel!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        bind.lifecycleOwner = this

        initViewModel()
        //将bind与xml声明的variable绑定
        val initVariableId = initVariableId()
        initVariableId?.let {
            bind.setVariable(initVariableId, viewModel)

        }
        //设置沉浸式状态栏
        statusBarColor?.let { color ->
            initStatusBarStyle(false, Color.parseColor(color))
        }
        initObserver()
        initView()
        initData()

    }

    /**
     * 初始化ViewModel
     */
    private fun initViewModel() {
        val viewModelClass = ClassUtil.getViewModel<VM>(this)
        if (viewModelClass != null) {
            _viewModel = ViewModelProvider(this).get(viewModelClass)
        }
    }

    override fun onClick(v: View?) {
        onDebounceClick(v)
    }

//    override fun getResources(): Resources {
//        var res = super.getResources()
//        val config = Configuration()
//        config.setToDefaults()
//        res.updateConfiguration(config, res.displayMetrics)
//        return res
//    }

    /** 消除抖动点击事件，子类绑定点击事件需要重写 */
    open fun onDebounceClick(v: View?) {}

    /**
     * 绑定控件点击事件，防止重复点击
     */
    fun applyDebounceClickListener(vararg views: View) {
        ClickUtils.applyGlobalDebouncing(views, this)
        for (v in views) {
            ClickUtils.applyPressedBgDark(v)
        }
    }

    @CallSuper
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            //打印出x和y的坐标位置
            if (KeyboardUtils.isShouldHideKeyBord(view, ev)) {
                KeyboardUtils.hintKeyBoards(view)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}