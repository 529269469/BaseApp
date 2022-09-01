package com.example.baseapp.ui

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.baseapp.BR
import com.example.baseapp.R
import com.example.baseapp.base_app.base.BaseMVVMActivity
import com.example.baseapp.base_app.base.BaseViewModel
import com.example.baseapp.databinding.ActivityMainBinding

class MainActivity : BaseMVVMActivity<ActivityMainBinding, BaseViewModel>(R.layout.activity_main) {
    override fun initObserver() {

    }

    override fun initData() {

    }

    override fun initView() {


    }

    override fun initVariableId()= BR.main


    inner class ListAdapter(data: MutableList<String>) :BaseQuickAdapter<String,BaseViewHolder>(R.layout.layout_adapter_main_list,data){
        override fun convert(holder: BaseViewHolder, item: String) {



        }

    }

}

