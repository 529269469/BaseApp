package com.example.psychology.ui.fragment

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.Navigation
import androidx.room.Room
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMFragment
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.base_app.utils.CreationChart
import com.example.psychology.base_app.utils.VideoPlayerUtils
import com.example.psychology.databinding.FragmentTrainingBinding
import com.example.psychology.romm.base.AppDatabase
import com.example.psychology.romm.entity.VideoData


/**
 * 视频播放
 */
class TrainingFragment :
    BaseMVVMFragment<FragmentTrainingBinding, BaseViewModel>(
        R.layout.fragment_training
    ) {
    override fun initObserver() {
    }

    var id: Long = 0
    private var videoData: VideoData? = null
    override fun initData() {
        id = arguments?.getLong("id")!!
        val db = Room.databaseBuilder(
            requireActivity(),
            AppDatabase::class.java, "users_dp"
        ).build()
        Thread {
            val videoDataDao = db.videoDataDao()
            videoData = videoDataDao.loadByIds(id)
        }.start()

    }


    var player: ExoPlayer? = null
    override fun initView() {
        applyDebouncingClickListener(
            bind.llTrainingLeft,
            bind.btTraining1,
            bind.btTraining2
        )
        setLineData()

        player = ExoPlayer.Builder(requireActivity()).build()
        bind.pvTraining.player = player

        bind.rvLineChartClose.setOnClickListener {
            if (bind.llChartTraining.visibility==View.GONE){
                bind.llChartTraining.visibility=View.VISIBLE
            }else{
                bind.llChartTraining.visibility=View.GONE
            }
        }
    }

    private fun setPlay() {
        //填充媒体数据
        player?.addMediaItem(MediaItem.fromUri(videoData?.video_path!!))
        //准备播放
        player?.prepare()
        //准备完成就开始播放
        player?.playWhenReady = true



        bind.btTraining1.visibility = View.GONE
        bind.btTraining2.visibility = View.GONE

    }

    override fun onDestroy() {
        super.onDestroy()
        VideoPlayerUtils.getInstance().setClose()
    }


    var setChart: CreationChart? = null

    private fun setLineData() {
        setChart = CreationChart(bind.chartTraining)
        setChart?.init()
        Thread {
            while (true) {
                Thread.sleep(500)
                runOnUiThread {
                    setChart?.AddData(
                        (Math.random() * 100).toFloat(),
                        (Math.random() * 100).toFloat()
                    )
                }
            }
        }.start()

    }

    override fun onDebouncingClick(v: View?) {
        super.onDebouncingClick(v)
        when (v) {
            bind.llTrainingLeft -> {
                Navigation.findNavController(v).navigateUp()
            }

            bind.btTraining1 -> {
                freeTraining("温馨提示：自由训练不会训练生成报告", 1)

            }

            bind.btTraining2 -> {
                freeTraining("温馨提示：自由训练不会训练生成报告", 2)
            }
        }
    }

    private var breatheWindow: PopupWindow? = null


    var freeWindow: PopupWindow? = null

    @SuppressLint("MissingInflatedId")
    private fun freeTraining(content: String, type: Int) {
        freeWindow = PopupWindow(activity)
        val inflate = layoutInflater.inflate(R.layout.layout_popup_free, null)
        freeWindow?.contentView = inflate
        freeWindow?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        freeWindow?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        freeWindow?.setBackgroundDrawable(ColorDrawable(0))
        freeWindow?.isFocusable = true
        freeWindow?.isOutsideTouchable = true
        val lp: WindowManager.LayoutParams = activity?.window!!.attributes
        lp.alpha = 0.7f
        activity?.window?.attributes = lp

        freeWindow?.setOnDismissListener {
            val lp1: WindowManager.LayoutParams = activity?.window!!.attributes
            lp1.alpha = 1f
            activity?.window?.attributes = lp1
            when (type) {
                1 -> {
                    setPlay()
                }

                2 -> {
                    setPlay()
                }
            }

        }
        freeWindow?.showAtLocation(bind.btTraining1, Gravity.CENTER, 0, 0)

        inflate.findViewById<ImageView>(R.id.iv_free_popup_x)
            .setOnClickListener { freeWindow?.dismiss() }
        inflate.findViewById<Button>(R.id.bt_free_yes)
            .setOnClickListener { freeWindow?.dismiss() }

        inflate.findViewById<TextView>(R.id.tv_free_content).text = content

    }


    override fun initVariableId() = BR.training


    override fun onStop() {
        super.onStop()
        VideoPlayerUtils.getInstance().setClose()
    }

}