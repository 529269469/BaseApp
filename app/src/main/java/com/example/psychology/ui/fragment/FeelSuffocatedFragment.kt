package com.example.psychology.ui.fragment

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.navigation.Navigation
import com.blankj.utilcode.util.SDCardUtils
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMFragment
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.base_app.expand.loge
import com.example.psychology.databinding.FragmentFeelSuffocatedBinding
import java.util.Timer
import java.util.TimerTask


/**
 * 憋气训练
 */
class FeelSuffocatedFragment :
    BaseMVVMFragment<FragmentFeelSuffocatedBinding, BaseViewModel>(
        R.layout.fragment_feel_suffocated
    ) {
    override fun initObserver() {
    }

    private var sleepNum: Long = 5000
    var scaleAnim: ScaleAnimation? = null
    var scaleAnim2: ScaleAnimation? = null
    override fun initData() {
        //放大动画
        scaleAnim = ScaleAnimation(
            1f,
            2f,
            1f,
            2f,
            ScaleAnimation.RELATIVE_TO_SELF,
            0.5f,
            ScaleAnimation.RELATIVE_TO_SELF,
            0.5f
        )
        scaleAnim?.duration = 5000
        scaleAnim?.fillAfter = true

        scaleAnim2 = ScaleAnimation(
            2f,
            1f,
            2f,
            1f,
            ScaleAnimation.RELATIVE_TO_SELF,
            0.5f,
            ScaleAnimation.RELATIVE_TO_SELF,
            0.5f
        )
        scaleAnim2?.duration = 5000
        scaleAnim2?.fillAfter = true

        scaleAnim?.setAnimationListener(startAnimation)
    }


    override fun initView() {
        applyDebouncingClickListener(
            bind.llFeelSuffocatedLeft,
            bind.tvFeelLung
        )
        setPlay()
    }

    private var player: MediaPlayer? = null

    private fun setPlay() {
        //storage/emulated/0/Android/data/com.example.psychology/files/psychology
        val video_path =
            SDCardUtils.getSDCardPathByEnvironment() + "/Movies/生理反馈/软件背景视频/呼吸界面背景视频.mp4"
        val holder = bind.svFeel.holder
        player = MediaPlayer()
        player?.setDataSource(requireActivity(), Uri.parse(video_path))
        player?.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
        player?.prepare()
        player?.setOnPreparedListener {
            player?.start()
            player?.isLooping = true
        }
        Thread {
            Thread.sleep(1000)
            activity?.runOnUiThread {
                player?.pause()
                player?.start()
            }
        }.start()
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                player?.setDisplay(holder); }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (player != null) {
            player?.stop()
            player?.reset()
            player?.release()
            player = null
        }
    }

    private var isPlay = false
    private var breatheWindow: PopupWindow? = null

    @SuppressLint("MissingInflatedId")
    override fun onDebouncingClick(v: View?) {
        super.onDebouncingClick(v)
        when (v) {
            bind.llFeelSuffocatedLeft -> {
                Navigation.findNavController(v).navigateUp()
            }

            bind.tvFeelLung -> {
                if (!isPlay) {
                    isPlay = true
                    popup()
                } else {
                    isPlay = false
                    bind.tvFeelLung.clearAnimation()
                    scaleAnim2?.cancel()
                    scaleAnim?.cancel()
                    timer?.cancel() // 取消计时器
                    timer = null
                    bind.tvFeelLung.text = "开始"
                }
            }
        }
    }

    private fun popup() {
        breatheWindow = PopupWindow(activity)
        val inflate = layoutInflater.inflate(R.layout.layout_popup_breathe, null)
        breatheWindow?.contentView = inflate
        breatheWindow?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        breatheWindow?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        breatheWindow?.setBackgroundDrawable(ColorDrawable(0))
        breatheWindow?.isFocusable = true
        breatheWindow?.isOutsideTouchable = true
        val lp: WindowManager.LayoutParams = activity?.window!!.attributes
        lp.alpha = 0.7f
        activity?.window?.attributes = lp

        breatheWindow?.setOnDismissListener {
            val lp1: WindowManager.LayoutParams = activity?.window!!.attributes
            lp1.alpha = 1f
            activity?.window?.attributes = lp1
        }
        breatheWindow?.showAtLocation(bind.tvFeelLung, Gravity.CENTER, 0, 0)

        val v5 = inflate.findViewById<View>(R.id.v_popup_breath_5)
        val v10 = inflate.findViewById<View>(R.id.v_popup_breath_10)
        val v15 = inflate.findViewById<View>(R.id.v_popup_breath_15)
        val v20 = inflate.findViewById<View>(R.id.v_popup_breath_20)
        val iv_compile_popup_x = inflate.findViewById<ImageView>(R.id.iv_compile_popup_breath_x)
        val tv_popup_breathe_begin = inflate.findViewById<TextView>(R.id.tv_popup_breathe_begin)


        tv_popup_breathe_begin.setOnClickListener {
            bind.tvFeelLung.startAnimation(scaleAnim)
            breatheWindow?.dismiss()
        }

        iv_compile_popup_x.setOnClickListener {
            breatheWindow?.dismiss()
            isPlay = false
        }
        sleepNum = 5000
        v5.setOnClickListener {
            sleepNum = 5000
            v10.setBackgroundResource(R.drawable.shape_feel2)
            v15.setBackgroundResource(R.drawable.shape_feel2)
            v20.setBackgroundResource(R.drawable.shape_feel5)
        }

        v10.setOnClickListener {
            sleepNum = 10000
            v10.setBackgroundResource(R.drawable.shape_feel3)
            v15.setBackgroundResource(R.drawable.shape_feel2)
            v20.setBackgroundResource(R.drawable.shape_feel5)
        }
        v15.setOnClickListener {
            sleepNum = 15000
            v10.setBackgroundResource(R.drawable.shape_feel3)
            v15.setBackgroundResource(R.drawable.shape_feel3)
            v20.setBackgroundResource(R.drawable.shape_feel5)
        }
        v20.setOnClickListener {
            sleepNum = 20000
            v10.setBackgroundResource(R.drawable.shape_feel3)
            v15.setBackgroundResource(R.drawable.shape_feel3)
            v20.setBackgroundResource(R.drawable.shape_feel4)
        }
    }


    @SuppressLint("HandlerLeak")
    var handler: Handler = object : Handler() {
        @SuppressLint("HandlerLeak")
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> {
                    bind.tvFeelLung.text = "憋气"
                }

                2 -> {
                    bind.tvFeelLung.text = "呼气"
                    //缩小动画
                    scaleAnim2?.setAnimationListener(endAnimation)
                    bind.tvFeelLung.startAnimation(scaleAnim2)

                }

                3 -> {
                    bind.tvFeelLung.text = "憋气\n${(remainingTime / 1000) + 1}"
                }

            }

        }
    }
    var timer: Timer? = null
    var remainingTime: Long = 0
    val startAnimation = object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            bind.tvFeelLung.text = "吸气"
        }

        override fun onAnimationEnd(animation: Animation?) {
            if (isPlay) {
                remainingTime = sleepNum
                bind.tvFeelLung.text = "憋气"
                timer = Timer()
                val timerTask = object : TimerTask() {
                    override fun run() {

                        if (remainingTime <= 0) {
                            handler.sendEmptyMessage(2)
                            timer?.cancel() // 取消计时器
                        } else {
                            println("剩余时间：${remainingTime / 1000} 秒")
                            remainingTime -= 1000
                            handler.sendEmptyMessage(3)
                        }

                    }
                }
                timer?.scheduleAtFixedRate(timerTask, 0, 1000)
            }else{
                bind.tvFeelLung.text = "开始"
            }

        }

        override fun onAnimationRepeat(animation: Animation?) {
        }

    }

    val endAnimation = object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
        }

        override fun onAnimationEnd(animation: Animation?) {
            bind.tvFeelLung.text = "吸气"
            scaleAnim?.setAnimationListener(startAnimation)
            bind.tvFeelLung.startAnimation(scaleAnim)
        }

        override fun onAnimationRepeat(animation: Animation?) {
        }

    }

    override fun initVariableId() = BR.feel_suffocated

}