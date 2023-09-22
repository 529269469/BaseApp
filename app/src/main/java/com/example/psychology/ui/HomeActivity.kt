package com.example.psychology.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cusoft.android.bledevice.control.BLEDevice
import com.cusoft.android.bledevice.util.PublicPrintLog
import com.cusoft.android.egox.EgoXDevice
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMActivity
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.bean.DeleteEvent
import com.example.psychology.databinding.ActivityHomeBinding
import com.example.psychology.music_utils.MusicFragment.handler
import com.example.psychology.ui.LoginActivity.Companion.head
import com.example.psychology.ui.LoginActivity.Companion.name
import com.example.psychology.ui.fragment.TrainingFragment2
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 主页
 */
class HomeActivity : BaseMVVMActivity<ActivityHomeBinding, BaseViewModel>(R.layout.activity_home) {

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: DeleteEvent) {
        if (event.head != 0) {
            bind.ivHomeHead.setImageResource(event.head)
        }
        if (event.title != null && event.title != "") {
            bind.tvHomeName.text = event.title
        }
    }

    override fun initObserver() {


    }

    override fun initData() {
        bind.ivHomeHead.setImageResource(head!!)
        bind.tvHomeName.text = name
    }

    var isPlaying=0

    private var homeAdapter: HomeAdapter? = null
    private val list = mutableListOf<CatalogBean>()
    private var controll: NavController? = null
    override fun initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        val linearLayoutManager: LinearLayoutManager = object : LinearLayoutManager(this) {
            //禁止竖向滑动 RecyclerView 为垂直状态（VERTICAL）
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        bind.rvHomeCatalog.layoutManager = linearLayoutManager;
        homeAdapter = HomeAdapter(mutableListOf())
        bind.rvHomeCatalog.adapter = homeAdapter

        list.add(CatalogBean("放松", R.mipmap.icon_relax, R.mipmap.icon_relax2, true))
        list.add(CatalogBean("冥想", R.mipmap.icon_meditation, R.mipmap.icon_meditation2, false))
        list.add(CatalogBean("报告", R.mipmap.icon_report, R.mipmap.icon_report2, false))
        list.add(CatalogBean("我的", R.mipmap.icon_wode, R.mipmap.icon_wode2, false))

        controll = Navigation.findNavController(this, R.id.flHome)

        homeAdapter?.setList(list)

        homeAdapter?.setOnItemClickListener { _, _, position ->


            list.forEach { it.isBool = false }
            list[position].isBool = true
            homeAdapter?.setList(list)
            when (position) {
                0 -> {
                    controll?.navigate(R.id.RelaxFragment)
                }

                1 -> {
                    controll?.navigate(R.id.MeditationFragment)
                }

                2 -> {
                    controll?.navigate(R.id.BreatheFragment)
                }

                3 -> {
                    controll?.navigate(R.id.MyFragment)
                }

            }

        }

        applyDebounceClickListener(
            bind.ivHomeAfterSale,
            bind.ivHomeHead
        )

    }


    override fun initVariableId() = BR.home


    override fun onDebounceClick(v: View?) {
        super.onDebounceClick(v)
        when (v) {
            bind.ivHomeAfterSale -> {
                afterSale()
            }

            bind.ivHomeHead -> {
                list.forEach { it.isBool = false }
                list[3].isBool = true
                homeAdapter?.setList(list)
                controll?.navigate(R.id.MyFragment)
            }
        }
    }

    /**
     * 售后客服
     */
    var afterSaleWindow: PopupWindow? = null
    private fun afterSale() {
        afterSaleWindow = PopupWindow(this)
        val inflate = layoutInflater.inflate(R.layout.layout_popup_after_sale, null)
        afterSaleWindow?.contentView = inflate
        afterSaleWindow?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        afterSaleWindow?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        afterSaleWindow?.setBackgroundDrawable(ColorDrawable(0))
        afterSaleWindow?.isFocusable = true
        afterSaleWindow?.isOutsideTouchable = true
        val lp: WindowManager.LayoutParams = window!!.attributes
        lp.alpha = 0.7f
        window?.attributes = lp

        afterSaleWindow?.setOnDismissListener {
            val lp1: WindowManager.LayoutParams = window!!.attributes
            lp1.alpha = 1f
            window?.attributes = lp1
        }
        afterSaleWindow?.showAtLocation(bind.ivHomeAfterSale, Gravity.CENTER, 0, 0)

        inflate.findViewById<ImageView>(R.id.iv_home_popup_x)
            .setOnClickListener { afterSaleWindow?.dismiss() }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        if (afterSaleWindow != null) {
            afterSaleWindow?.dismiss()
        }
    }


    inner class HomeAdapter(data: MutableList<CatalogBean>) :
        BaseQuickAdapter<CatalogBean, BaseViewHolder>(R.layout.layout_adapter_home, data) {
        override fun convert(holder: BaseViewHolder, item: CatalogBean) {
            holder.setText(R.id.tvHomeCatalog, item.title)
                .setTextColor(
                    R.id.tvHomeCatalog,
                    if (item.isBool) resources.getColor(R.color.white) else resources.getColor(R.color.color_869397)
                )
                .setBackgroundResource(
                    R.id.llHomeCatalog, if (item.isBool) R.drawable.shape_home_catalog1
                    else R.drawable.shape_home_catalog2
                )
                .setImageResource(
                    R.id.ivHomeCatalog, if (item.isBool) item.icon2
                    else item.icon
                )

        }

    }

    data class CatalogBean(
        val title: String,
        val icon: Int,
        val icon2: Int,
        var isBool: Boolean
    )


}