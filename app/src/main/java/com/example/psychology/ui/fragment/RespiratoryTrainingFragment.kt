package com.example.psychology.ui.fragment

import android.media.MediaPlayer
import android.net.Uri
import android.view.SurfaceHolder
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.navigation.Navigation
import cn.com.heaton.blelibrary.ble.request.Rproxy.release
import com.blankj.utilcode.util.SDCardUtils
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMFragment
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.base_app.expand.loge
import com.example.psychology.databinding.FragmentRespiratoryTrainingBinding


/**
 * 呼吸训练
 */
class RespiratoryTrainingFragment :
    BaseMVVMFragment<FragmentRespiratoryTrainingBinding, BaseViewModel>(
        R.layout.fragment_respiratory_training
    ) {
    override fun initObserver() {

    }

    var isAnimation = true
    var timecurrentTimeMillis:Long=0
    var scaleAnim:ScaleAnimation?=null
    override fun initData() {
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

        val scaleAnim2 = ScaleAnimation(
            2f,
            1f,
            2f,
            1f,
            ScaleAnimation.RELATIVE_TO_SELF,
            0.5f,
            ScaleAnimation.RELATIVE_TO_SELF,
            0.5f
        )
        scaleAnim2.duration = 5000
        scaleAnim2.fillAfter = true

        scaleAnim?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                bind.tvRespiratoryLung.text = "吸气"
            }

            override fun onAnimationEnd(animation: Animation?) {
                if (!isAnimation) {
                    bind.tvRespiratoryLung.startAnimation(scaleAnim2)
                }

            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
        scaleAnim2.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                bind.tvRespiratoryLung.text = "呼气"
            }

            override fun onAnimationEnd(animation: Animation?) {
                if (!isAnimation) {
                    bind.tvRespiratoryLung.startAnimation(scaleAnim)
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })

    }

    override fun initView() {
        applyDebouncingClickListener(
            bind.llRespiratoryTrainingLeft,
            bind.tvRespiratoryLung
        )
        setPlay()
    }

    override fun onDebouncingClick(v: View?) {
        super.onDebouncingClick(v)
        when(v){
            bind.llRespiratoryTrainingLeft->{
                Navigation.findNavController(v).navigateUp()
            }
            bind.tvRespiratoryLung->{
                if (isAnimation) {
                    bind.tvRespiratoryLung.text = "停止"
                    isAnimation = false
                    bind.tvRespiratoryLung.startAnimation(scaleAnim)

                    timecurrentTimeMillis = System.currentTimeMillis()
                } else {
                    isAnimation = true
                    bind.tvRespiratoryLung.text = "开始"
                    bind.tvRespiratoryLung.clearAnimation()

                }
            }
        }
    }
    private var player: MediaPlayer? = null
    private fun setPlay() {
        val video_path = SDCardUtils.getSDCardPathByEnvironment() + "/Movies/生理反馈/软件背景视频/呼吸界面背景视频.mp4"
        val holder = bind.svFeel.holder
        player = MediaPlayer()
        player?.setDataSource(requireActivity(), Uri.parse(video_path))
        player?.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
        player?.prepare()
        player?.setOnPreparedListener {
            player?.start()
            player?.isLooping = true
        }
        Thread{
            Thread.sleep(1000)
            activity?.runOnUiThread {
                player?.pause()
                player?.start()
            }
        }.start()
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) { player?.setDisplay(holder); }
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            }
            override fun surfaceDestroyed(holder: SurfaceHolder) {}
        })


    }

    override fun initVariableId() = BR.respiratory_training

    override fun onDestroyView() {
        super.onDestroyView()
        if (player!=null){
            player?.stop()
            player?.reset()
            player?.release()
            player = null
        }
    }

}