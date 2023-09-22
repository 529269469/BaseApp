package com.example.psychology.ui.my

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMActivity
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.base_app.utils.SpacesItemDecoration
import com.example.psychology.databinding.ActivityMyBinding
import com.example.psychology.ui.LoginActivity
import com.example.psychology.ui.LoginActivity.Companion.head
import com.example.psychology.ui.LoginActivity.Companion.name

class MyActivity :BaseMVVMActivity<ActivityMyBinding,BaseViewModel>(R.layout.activity_my){
    override fun initObserver() {
    }

    override fun initData() {
        bind.ivMyAcHead.setImageResource(head!!)
        bind.tvMyAcName.text= name
    }


    private var myTabAdapter: MyTabAdapter? = null
    private val list = mutableListOf<TabData>()
    var type=0
    override fun initView() {
        type=intent.getIntExtra("type",0)

        bind.rvMyCatalogue.layoutManager = GridLayoutManager(this, 1)
        myTabAdapter = MyTabAdapter(mutableListOf())
        bind.rvMyCatalogue.adapter = myTabAdapter
        list.add(TabData(R.mipmap.icon_record, R.mipmap.icon_record2,"我的记录",false))
        list.add(TabData(R.mipmap.icon_collect,R.mipmap.icon_collect2, "我的收藏",false))
        list.add(TabData(R.mipmap.icon_report1,R.mipmap.icon_report3, "我的报告",false))
        if (LoginActivity.isAdmin!!){
            list.add(TabData(R.mipmap.icon_course,R.mipmap.icon_course2, "我的课程",false))
            list.add(TabData(R.mipmap.icon_data,R.mipmap.icon_data2, "数据中心",false))
        }

        bind.rvMyCatalogue.addItemDecoration(SpacesItemDecoration(8))
        var controll = Navigation.findNavController(this, R.id.flMyTab) as NavController;
        list[type].isBool=true
        myTabAdapter?.setList(list)
        setWhen(type,controll)

        myTabAdapter?.setOnItemClickListener { _, _, position ->
            list.forEach { it.isBool=false }
            list[position].isBool=true
            myTabAdapter?.setList(list)
            setWhen(position,controll)
        }

        bind.ivMyLeft.setOnClickListener { finish() }
    }

    private fun setWhen(type: Int, controll: NavController) {
        when(type){
            0->{//我的记录
                val bundle = Bundle()
                bundle.putInt("catalogue",1)
                controll.navigate(R.id.MyListFragment,bundle)
            }
            1->{
                //我的收藏
                val bundle = Bundle()
                bundle.putInt("catalogue",2)
                controll.navigate(R.id.MyListFragment,bundle)
            }
            2->{
                controll.navigate(R.id.BreatheFragment )
            }
            3->{
                controll.navigate(R.id.courseFragment )
            }
            4->{
                controll.navigate(R.id.dataFragment )
            }
        }
    }

    override fun initVariableId()= BR.my



    inner class MyTabAdapter(data: MutableList<TabData>) :
        BaseQuickAdapter<TabData, BaseViewHolder>(R.layout.layout_adapter_my_tab, data) {
        override fun convert(holder: BaseViewHolder, item: TabData) {
            holder.setText(R.id.tv_my_tab_title, item.title)
                .setTextColor(R.id.tv_my_tab_title,if (item.isBool) resources.getColor(R.color.white) else resources.getColor(R.color.color_B3B3B3))
                .setBackgroundResource(R.id.ll_may_tab,if (item.isBool) R.drawable.shape_jianbian_10_a4deed
                else R.drawable.shape_login_white_10)
                .setImageResource(R.id.iv_my_tab_icon, if (item.isBool) item.icon2
                else item.icon)

        }

    }

    data class  TabData(
        val icon:Int,
        val icon2:Int,
        val title:String,
        var isBool:Boolean
    )
}