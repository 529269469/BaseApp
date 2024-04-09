package com.example.psychology.ui

import android.Manifest
import android.app.ActionBar
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil.setContentView
import cn.com.heaton.blelibrary.ble.Ble
import com.blankj.utilcode.util.SDCardUtils
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMActivity
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.base_app.expand.loge
import com.example.psychology.base_app.utils.FileUtils
import com.example.psychology.base_app.utils.PermissionXUtils.setPermission
import com.example.psychology.databinding.ActivityLadingBinding
import kotlin.math.ceil

class LadingActivity : BaseMVVMActivity<ActivityLadingBinding,BaseViewModel> (R.layout.activity_lading){

    override fun initObserver() {
    }

    override fun initData() {

    }

    override fun initView() {
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        val actionBar: ActionBar? = actionBar
        actionBar?.hide()

        requestBLEPermission()
        setPermission {

        }
        setPlay()
//        Thread{
//            Thread.sleep(2000)
//            runOnUiThread {
//                startActivity(Intent(this, LoginActivity::class.java))
//                finish()
//            }
//        }.start()

    }





    private var player: MediaPlayer? = null

    private fun setPlay() {
        val video_path = SDCardUtils.getSDCardPathByEnvironment() + "/Movies/生理反馈/软件背景视频/"
        val holder = bind.svLading.holder
        player = MediaPlayer()
        player?.setDataSource(this, Uri.parse("${video_path}登录界面背景视频2.mp4"))
        player?.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)

        player?.prepare()
        player?.setOnPreparedListener {
            player?.start()
            player?.isLooping = false
            Thread{
                Thread.sleep(2000)
                runOnUiThread {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }.start()
        }


        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                player?.setDisplay(holder)
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                "surfaceDestroyed".loge()
            }

        })
    }
    override fun initVariableId()=BR.lading




    override fun onDestroy() {
        super.onDestroy()
        if (player!=null){
            player?.stop()
            player?.reset()
            player?.release()
            player=null
        }
    }

    override fun onStop() {
        super.onStop()
        if (player!=null){
            player?.stop()
            player?.reset()
            player?.release()
            player=null
        }
    }
    private val REQUEST_CODE: Int = 0x01
    private fun requestBLEPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.RECORD_AUDIO,
            ), REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    val b = shouldShowRequestPermissionRationale(permissions[0])
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                    } else
                        finish()
                } else {
                    //权限申请成功
                    initBLE()
                }
            }
        }

    }

    private fun initBLE() {
        //3、检查蓝牙是否支持及打开
        checkBluetoothStatus()
    }

    private fun checkBluetoothStatus() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val i = ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                } else {
                    //权限申请成功
                    initBLE()
                }
            }
        }
    }
}