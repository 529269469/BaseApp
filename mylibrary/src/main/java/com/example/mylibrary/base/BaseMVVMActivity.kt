package com.example.mylibrary.base

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.example.mylibrary.utils.base.clickutils.ClickUtils
import com.example.mylibrary.utils.base.vmutils.ClassUtil
import com.example.mylibrary.utils.statusbar.StatusBar.initStatusBarStyle

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

    override fun getResources(): Resources {
        var res = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.displayMetrics)
        return res
    }

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

}