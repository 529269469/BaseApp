package com.example.psychology.ui

import android.annotation.SuppressLint
import android.app.ActionBar
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import androidx.room.Room
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMActivity
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.base_app.expand.loge
import com.example.psychology.base_app.utils.CreationChart
import com.example.psychology.bean.DeleteEvent
import com.example.psychology.bean.HrvEvent
import com.example.psychology.bean.MessageEvent
import com.example.psychology.databinding.ActivityPlayBinding
import com.example.psychology.romm.base.AppDatabase
import com.example.psychology.romm.entity.BrainData
import com.example.psychology.romm.entity.RadarChartData
import com.example.psychology.romm.entity.VideoData
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.Timer
import java.util.TimerTask


class PlayActivity : BaseMVVMActivity<ActivityPlayBinding, BaseViewModel>(R.layout.activity_play) {
    private val TAG = "TAG"

    override fun initObserver() {

    }

    override fun initData() {

    }

    @SuppressLint("HandlerLeak")
    var handler: Handler = object : Handler() {
        @SuppressLint("HandlerLeak")
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (mediaPlayer != null) {
                val current: Int? = mediaPlayer?.currentPosition
                val duration: Int? = mediaPlayer?.duration
                val format = SimpleDateFormat("mm:ss")
                bind.tvPlayTime.text = format.format(current)
                bind.tvPlayTime2.text = format.format(duration)
                mediaPlayer?.duration?.let { bind.pbPlayProgress.max = it }
                mediaPlayer?.currentPosition?.let { bind.pbPlayProgress.progress = it }
                if (current!! > (duration!! - 1000)) {
                    val deleteEvent = DeleteEvent()
                    deleteEvent.seekto = mediaPlayer?.currentPosition!!
                    deleteEvent.number=number
                    EventBus.getDefault().post(deleteEvent)
                    finish()
                }
            }
        }
    }
    var id: Long? = null
    var seekTo: Int = 0
    private var videoData: VideoData? = null
    private var mediaPlayer: MediaPlayer? = null
    private var video_path: String? = null
    var holder: SurfaceHolder? = null

    var setChart: CreationChart? = null
    private var reportId: Long = 0
    override fun initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        val actionBar: ActionBar? = actionBar
        actionBar?.hide()

        id = intent.getLongExtra("id", 0)
        reportId = intent.getLongExtra("reportId", 0)
        seekTo = intent.getIntExtra("seekTo", 0)
        video_path = intent.getStringExtra("video_path")
        number = intent.getIntExtra("number",0)

        "$seekTo   $video_path".loge()
        val db = Room.databaseBuilder<AppDatabase>(
            this,
            AppDatabase::class.java, "users_dp"
        ).build()
        Thread {
            val videoDataDao = db.videoDataDao()
            videoData = videoDataDao.loadByIds(id!!)
        }.start()
        holder = bind.pvPlay.holder

        setPlay()
        Thread {
            Thread.sleep(500)
            runOnUiThread {
                if (mediaPlayer != null) {
                    mediaPlayer?.setDisplay(holder)
                    mediaPlayer?.seekTo(seekTo)
                    mediaPlayer?.start()
                    bind.ivPlayPlay.setImageResource(R.mipmap.icon_pause3)
                }
            }
        }.start()

        setChart = CreationChart(bind.chartPlay)
        setChart?.init()


        bind.ivPlayPlay.setOnClickListener {
            if (mediaPlayer != null) {
                if (mediaPlayer?.isPlaying!!) {
                    mediaPlayer?.pause()
                    bind.ivPlayPlay.setImageResource(R.mipmap.icon_training_play)
                } else {
                    bind.ivPlayPlay.setImageResource(R.mipmap.icon_pause3)
                    mediaPlayer?.setDisplay(holder)
                    mediaPlayer?.start()
                }
            }else{
                setPlay()
                bind.ivPlayPlay.setImageResource(R.mipmap.icon_pause3)
                mediaPlayer?.setDisplay(holder)
                mediaPlayer?.start()
            }
        }

        bind.ivPlayStop.setOnClickListener {
            if (mediaPlayer != null) {
                bind.ivPlayPlay.setImageResource(R.mipmap.icon_training_play)
                mediaPlayer?.seekTo(0)
                mediaPlayer?.pause()
            }
            val deleteEvent = DeleteEvent()
            deleteEvent.seekto = mediaPlayer?.currentPosition!!
            deleteEvent.isStop = true
            deleteEvent.number=number
            EventBus.getDefault().post(deleteEvent)
            finish()

        }

        bind.llplayLeft.setOnClickListener {
            val deleteEvent = DeleteEvent()
            deleteEvent.seekto = mediaPlayer?.currentPosition!!
            deleteEvent.number=number
            EventBus.getDefault().post(deleteEvent)
            finish()
        }

        bind.ivPlayFull.setOnClickListener {
            val deleteEvent = DeleteEvent()
            deleteEvent.seekto = mediaPlayer?.currentPosition!!
            deleteEvent.number=number
            EventBus.getDefault().post(deleteEvent)
            finish()
        }

        bind.rvLineChartClose.setOnClickListener {
            if (bind.llChartPlay.visibility==View.VISIBLE){
                bind.llChartPlay.visibility=View.GONE
            }else{
                bind.llChartPlay.visibility=View.VISIBLE
            }
        }
    }

    private fun setPlay() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(this, Uri.parse(video_path))
        mediaPlayer?.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
        mediaPlayer?.prepare()

        mediaPlayer?.setOnPreparedListener { mp: MediaPlayer? ->
            mediaPlayer?.setDisplay(holder)
        }

        val timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                handler.sendEmptyMessage(10086)
            }
        }
        timer.schedule(timerTask, 500, 1000)
    }


    override fun initVariableId() = BR.play

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val deleteEvent = DeleteEvent()
        deleteEvent.seekto = mediaPlayer?.currentPosition!!
        deleteEvent.number=number
        EventBus.getDefault().post(deleteEvent)
        finish()
    }
    private var number = 0
    private var messageEvent: MessageEvent? = null

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onMessageEvent(event: MessageEvent) {
        this.messageEvent=event
        if (event.attention == 0 || event.meditation == 0) {
            return
        }
        setChart?.AddData(event.attention.toFloat(),event.meditation.toFloat())
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            val db = Room.databaseBuilder<AppDatabase>(
                this,
                AppDatabase::class.java, "users_dp"
            ).build()
            number++
            Thread {
                val radarChartDao = db.radarChartDao()
                radarChartDao.insertAll(
                    RadarChartData(reportId, 1, number, event.attention,0f),
                    RadarChartData(reportId, 2, number, event.meditation,0f)
                )

                val brainDao = db.brainDao()
                brainDao.insertAll(
                    BrainData(
                        reportId, number, event.delta,
                        event.theta,
                        event.lowAlpha,
                        event.highAlpha,
                        event.lowBeta,
                        event.highBeta,
                        event.lowGamma,
                        event.midGamma
                    )
                )
            }.start()
            Log.e("TAG", "handleMessage: 专注: ${event.attention}  meditation: 放松:${event.meditation}   number:$number  reportId:$reportId")
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: HrvEvent) {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            val db = Room.databaseBuilder<AppDatabase>(
                this,
                AppDatabase::class.java, "users_dp"
            ).build()
            Thread {
                val radarChartDao = db.radarChartDao()
                radarChartDao.insertAll(
                    RadarChartData(reportId, 3, number, 0, event.stress),
                    RadarChartData(reportId, 4, number, 0, event.emotion),
                    RadarChartData(reportId, 5, number, 0, event.fatigue),
                    RadarChartData(reportId, 6, number, event.heartRate, 0f),
                    RadarChartData(reportId, 7, number, event.hrv, 0f),
                    RadarChartData(reportId, 8, number, event.bloodOxygen, 0f)
                )
            }.start()
        }
    }

}