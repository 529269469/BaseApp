package com.example.psychology.ui.fragment

import android.content.Intent
import android.view.View
import androidx.navigation.Navigation
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMFragment
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.base_app.utils.CreationChartBrain
import com.example.psychology.bean.MessageEvent
import com.example.psychology.databinding.FragmentBrainWaveBinding
import com.example.psychology.ui.my.MyActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class BrainWaveFragment :
    BaseMVVMFragment<FragmentBrainWaveBinding, BaseViewModel>(R.layout.fragment_brain_wave) {
    override fun initObserver() {
    }

    override fun initData() {

    }
    var chartdelta: CreationChartBrain? = null
    var charttheta: CreationChartBrain? = null
    var chartlowAlpha: CreationChartBrain? = null
    var charthighAlpha: CreationChartBrain? = null
    var chartlowBeta: CreationChartBrain? = null
    var charthighBeta: CreationChartBrain? = null
    var chartlowGamma: CreationChartBrain? = null
    var chartmidGamma: CreationChartBrain? = null
    override fun initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        chartdelta = CreationChartBrain(bind.lcBrainWaveDelta,"δ波")
        chartdelta?.init()
        charttheta = CreationChartBrain(bind.lcBrainWaveTheta,"θ波")
        charttheta?.init()
        chartlowAlpha = CreationChartBrain(bind.lcBrainWaveLowAlpha,"low α波")
        chartlowAlpha?.init()
        charthighAlpha = CreationChartBrain(bind.lcBrainWaveHighAlpha,"high α波")
        charthighAlpha?.init()
        chartlowBeta = CreationChartBrain(bind.lcBrainWaveLowBeta,"low β波")
        chartlowBeta?.init()
        charthighBeta = CreationChartBrain(bind.lcBrainWaveHighBeta,"high β波")
        charthighBeta?.init()
        chartlowGamma = CreationChartBrain(bind.lcBrainWaveLowGamma,"low γ波")
        chartlowGamma?.init()
        chartmidGamma = CreationChartBrain(bind.lcBrainWaveMidGamma,"mid γ波")
        chartmidGamma?.init()


        applyDebouncingClickListener(
            bind.ivBrainFavorite,
            bind.ivBrainHistory,
            bind.llBrainLeft
        )

    }

    override fun onDebouncingClick(v: View?) {
        super.onDebouncingClick(v)
        when(v){
            bind.ivBrainFavorite->{
                startActivity(Intent(activity, MyActivity::class.java).putExtra("type",1))
            }
            bind.ivBrainHistory->{
                startActivity(Intent(activity, MyActivity::class.java).putExtra("type",0))
            }
            bind.llBrainLeft->{
                Navigation.findNavController(v).navigateUp()
            }
        }
    }

    override fun initVariableId() = BR.brain_wave


    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    private var messageEvent: MessageEvent? = null
    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onMessageEvent(event:MessageEvent ) {
        this.messageEvent=event
        chartdelta?.AddData(event.delta.toFloat())
        charttheta?.AddData(event.theta.toFloat())
        chartlowAlpha?.AddData(event.lowAlpha.toFloat())
        charthighAlpha?.AddData(event.highAlpha.toFloat())
        chartlowBeta?.AddData(event.lowBeta.toFloat())
        charthighBeta?.AddData(event.highBeta.toFloat())
        chartlowGamma?.AddData(event.lowGamma.toFloat())
        chartmidGamma?.AddData(event.midGamma.toFloat())


    }

}