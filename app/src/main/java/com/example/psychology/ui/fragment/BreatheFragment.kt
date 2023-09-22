package com.example.psychology.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ThreadUtils
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMFragment
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.base_app.utils.CreationChart
import com.example.psychology.base_app.utils.CreationChartBrain
import com.example.psychology.databinding.FragmentBrainWaveBinding
import com.example.psychology.databinding.FragmentBreatheBinding
import com.example.psychology.ui.my.MyActivity


class BreatheFragment :
    BaseMVVMFragment<FragmentBreatheBinding, BaseViewModel>(R.layout.fragment_breathe) {
    override fun initObserver() {
    }

    override fun initData() {


    }

    override fun initView() {
        applyDebouncingClickListener(
            bind.llBreatheLeft,
            bind.tvLung,
            bind.tvLung2
        )


    }

    override fun onDebouncingClick(v: View?) {
        super.onDebouncingClick(v)
        when(v){
            bind.llBreatheLeft->{
                Navigation.findNavController(v).navigateUp()
            }
            bind.tvLung->{
                Navigation.findNavController(v)
                    .navigate(R.id.action_breatheFragment_to_respiratoryTrainingFragment)
            }
            bind.tvLung2->{
                Navigation.findNavController(v)
                    .navigate(R.id.action_breatheFragment_to_feelSuffocatedFragment)
            }
        }
    }

    override fun initVariableId() = BR.breathe

}