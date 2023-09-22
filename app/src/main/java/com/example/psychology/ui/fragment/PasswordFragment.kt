package com.example.psychology.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.room.Room
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMFragment
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.base_app.utils.CreationChart
import com.example.psychology.base_app.utils.CreationChartBrain
import com.example.psychology.databinding.FragmentBrainWaveBinding
import com.example.psychology.databinding.FragmentBreatheBinding
import com.example.psychology.databinding.FragmentPasswordBinding
import com.example.psychology.romm.base.AppDatabase
import com.example.psychology.ui.my.MyActivity


class PasswordFragment :
    BaseMVVMFragment<FragmentPasswordBinding, BaseViewModel>(R.layout.fragment_password) {
    override fun initObserver() {

    }

    override fun initData() {


    }

    override fun initView() {

        applyDebouncingClickListener(
            bind.llPasswordLeft,
            bind.btPasswordBegin
        )
    }

    override fun onDebouncingClick(v: View?) {
        super.onDebouncingClick(v)
        when(v){
            bind.llPasswordLeft->{
                Navigation.findNavController(bind.llPasswordLeft).navigateUp()
            }
            bind.btPasswordBegin->{
                val db = Room.databaseBuilder(
                    requireActivity(),
                    AppDatabase::class.java, "users_dp"
                ).build()
                val etLoginSignIn = bind.etPasswordSignIn.text.toString()
                val etLoginPassword = bind.etPasswordPassword.text.toString()
                Thread{
                    val userDao = db.userDao()
                    val findByName = userDao.findByName(etLoginSignIn)
                    if (findByName == null) {
                        ToastUtils.showLong("用户名不存在，请仔细检查")
                    }else{
                        if (findByName.user_name=="youke"){
                            ToastUtils.showLong("不允许修改系统账号")
                        }else{
                            findByName.password=etLoginPassword
                            userDao.update(findByName)
                            ToastUtils.showLong("修改成功")
                        }

                    }
                }.start()

            }

        }
    }


    override fun initVariableId() = BR.password

}