package com.example.psychology.ui


import android.Manifest
import android.annotation.SuppressLint
import android.app.ActionBar
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceHolder.Callback
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import cn.com.heaton.blelibrary.ble.Ble
import cn.com.heaton.blelibrary.ble.BleLog
import cn.com.heaton.blelibrary.ble.callback.BleConnectCallback
import cn.com.heaton.blelibrary.ble.callback.BleMtuCallback
import cn.com.heaton.blelibrary.ble.callback.BleNotifyCallback
import cn.com.heaton.blelibrary.ble.callback.BleScanCallback
import cn.com.heaton.blelibrary.ble.model.BleDevice
import cn.com.heaton.blelibrary.ble.utils.ByteUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.SDCardUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMActivity
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.base_app.expand.loge
import com.example.psychology.base_app.utils.FileUtils
import com.example.psychology.base_app.utils.PermissionXUtils.setPermission
import com.example.psychology.ble.BleUtils
import com.example.psychology.databinding.ActivityLoginBinding
import com.example.psychology.romm.base.AppDatabase
import com.example.psychology.romm.entity.User
import com.example.psychology.romm.entity.VideoData
import com.example.psychology.ui.egoxsdk.AppProtocol
import com.example.psychology.ui.egoxsdk.MyBleWrapperCallback
import com.tencent.mmkv.MMKV
import com.youth.banner.util.LogUtils
import java.util.UUID


class LoginActivity :
    BaseMVVMActivity<ActivityLoginBinding, BaseViewModel>(R.layout.activity_login) {

    private val TAG = "LoginActivity"
    override fun initObserver() {
    }


    override fun initData() {
        setPlay()
        setDevice()
        setCopy()

    }



    private var player: MediaPlayer? = null

    private fun setPlay() {
        val video_path = SDCardUtils.getSDCardPathByEnvironment() + "/Movies/生理反馈/软件背景视频/"
        val holder = bind.pvLogin.holder
        player = MediaPlayer()
        player?.setDataSource(this, Uri.parse("${video_path}登录界面背景视频2.mp4"))
        player?.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
        player?.prepare()
        player?.setOnPreparedListener {
            player?.isLooping = true
            player?.setDisplay(holder)
            player?.start()
        }

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
        applyDebounceClickListener(
            bind.tvLoginSignIn,
            bind.tvLoginRegister,
            bind.btLoginBegin,
            bind.tvLoginTourist
        )
    }

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



    var isSing = false
    override fun onDebounceClick(v: View?) {
        super.onDebounceClick(v)
        when (v) {
            bind.tvLoginSignIn -> {
                isSing = false
                bind.tvLoginSignIn.setTextColor(getColor(R.color.black))
                bind.tvLoginRegister.setTextColor(getColor(R.color.color_login_2))
                bind.viewLoginLine1.visibility = View.VISIBLE
                bind.viewLoginLine2.visibility = View.GONE
                bind.btLoginBegin.text = "登录"

                val list = mBle.connectedDevices
                for (device in list) {
                    AppProtocol.write(device, ByteUtils.hexStr2Bytes("AA0B01000100010000800072"))
                }
            }

            bind.tvLoginRegister -> {
                isSing = true
                bind.tvLoginSignIn.setTextColor(getColor(R.color.color_login_2))
                bind.tvLoginRegister.setTextColor(getColor(R.color.black))
                bind.viewLoginLine1.visibility = View.GONE
                bind.viewLoginLine2.visibility = View.VISIBLE
                bind.btLoginBegin.text = "注册"
            }

            bind.btLoginBegin -> {
                val db = Room.databaseBuilder(
                    this,
                    AppDatabase::class.java, "users_dp"
                ).build()
                val etLoginSignIn = bind.etLoginSignIn.text.toString()
                val etLoginPassword = bind.etLoginPassword.text.toString()
                if (isSing) {
                    //注册
                    Thread {
                        val userDao = db.userDao()
                        val findByName = userDao.findByName(etLoginSignIn)
                        if (findByName == null) {
                            userDao.insertAll(
                                User(
                                    etLoginSignIn,
                                    etLoginSignIn,
                                    etLoginPassword,
                                    R.mipmap.icon_head11,
                                    "", false,System.currentTimeMillis(),0,0,0
                                )
                            )
                            denglu(
                                etLoginSignIn,
                                etLoginPassword
                            )
                        } else {
                            ToastUtils.showLong("用户名已存在")
                        }
                    }.start()

                } else {
                    //登录
                    denglu(
                        etLoginSignIn,
                        etLoginPassword
                    )
//                    denglu(
//                        "admin",
//                        "admin"
//                    )
                }
            }

            bind.tvLoginTourist -> {
                denglu(
                    "youke",
                    "123123"
                )

            }
        }
    }

    companion object {
        var user_name: String? = null
        var name: String? = null
        var signature: String? = null
        var password: String? = null
        var uid: Long? = null
        var head: Int? = null
        var isAdmin: Boolean? = null
    }

    fun denglu(user_name: String, password: String) {
        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "users_dp"
        ).build()

        Thread {
            val userDao = db.userDao()
            val user = userDao.findByPassword(user_name, password)
            if (user.isNotEmpty()) {
                LoginActivity.user_name = user[0].user_name
                LoginActivity.name = user[0].name
                LoginActivity.signature = user[0].signature
                LoginActivity.password = user[0].password
                LoginActivity.uid = user[0].uid
                LoginActivity.head = user[0].head
                LoginActivity.isAdmin = user[0].isAdmin

                if (TimeUtils.isToday(user[0].update_time?.plus(86400000)!!)) {
                    user[0].continuous_time = user[0].continuous_time?.plus(1)
                }

                if (!TimeUtils.isToday(user[0].update_time!!)) {
                    user[0].add_up_time = user[0].add_up_time?.plus(1)
                }
                user[0].update_time = System.currentTimeMillis()

                userDao.update(user[0])
                runOnUiThread {
                    startActivity(Intent(this, HomeJavcActivity::class.java))
                    finish()
                }
            } else {
                runOnUiThread {
                    ToastUtils.showLong("用户名或密码不正确")
                    bind.llLoginClose.visibility = View.VISIBLE
                    YoYo.with(Techniques.Shake)
                        .duration(500)
                        .repeat(1)
                        .playOn(bind.llLoginClose)
                    YoYo.with(Techniques.Shake)
                        .duration(500)
                        .repeat(1)
                        .playOn(bind.etLoginSignIn)
                    YoYo.with(Techniques.Shake)
                        .duration(500)
                        .repeat(1)
                        .playOn(bind.etLoginPassword)
                }
            }

        }.start()

    }


    /**
     * 复制资源
     */
    private fun setCopy() {
        val kv = MMKV.defaultMMKV()
        val assets = kv?.getBoolean("Assets", false)
        KeyboardUtils.hideSoftInput(this)
        if (!assets!!) {
            kv.putString("photo", "16566668888")
            val db = Room.databaseBuilder(
                this,
                AppDatabase::class.java, "users_dp"
            ).build()
            setData(db)
            //注册游客
            Thread {
                val userDao = db.userDao()
                userDao.insertAll(
                    User(
                        "youke",
                        "游客",
                        "123123",
                        R.mipmap.icon_head11,
                        "", false,System.currentTimeMillis(),0,0,0
                    )
                )
                userDao.insertAll(
                    User(
                        "admin",
                        "管理员",
                        "admin",
                        R.mipmap.icon_head11,
                        "", true,System.currentTimeMillis(),0,0,0
                    )
                )
            }.start()

            setPermission {
                FileUtils.getInstance(this).copyAssetsToSD("video", "psychology")
                    .setFileOperateCallback(object : FileUtils.FileOperateCallback {
                        override fun onSuccess() {
                            Log.e(TAG, "Assets:onSuccess")
                            kv.putBoolean("Assets", true)
                        }

                        override fun onFailed(error: String) {
                            Log.e(TAG, "Assets:onFailed")
                        }
                    })
            }

            kv.putBoolean("Assets", true)
        }

//        Thread{
//            val db = Room.databaseBuilder(
//                this,
//                AppDatabase::class.java, "users_dp"
//            ).build()
//            val videoDataDao = db.videoDataDao()
//            val video_path = SDCardUtils.getSDCardPathByEnvironment() + "/Movies/生理反馈/"
//            videoDataDao.insertAll(
//                VideoData(
//                    0,
//                    "佛教音乐",
//                    "佛教音乐的神秘韵律引领您进入冥想，减轻焦虑，感受内心的平静与安宁。",
//                    "${video_path}佛教音乐.mp4",
//                    "音乐放松",
//                    1,
//                    1,
//                    0
//                )
//            )
//        }.start()

    }

    private fun setData(db: AppDatabase) {
        Thread {
            val videoDataDao = db.videoDataDao()
            val video_path = SDCardUtils.getSDCardPathByEnvironment() + "/Movies/生理反馈/"
            videoDataDao.insertAll(
                VideoData(
                    0,
                    "阿尔法音乐",
                    "阿尔法音乐通过创新的旋律激发阿尔法脑波，帮助深度放松，提高专注力和创造力。",
                    "${video_path}阿尔法音乐.mp4",
                    "音乐放松",
                    1,
                    1,
                    0
                )
            )
            videoDataDao.insertAll(
                VideoData(
                    0,
                    "佛教音乐",
                    "佛教音乐的神秘韵律引领您进入冥想，减轻焦虑，感受内心的平静与安宁。",
                    "${video_path}佛教音乐.mp4",
                    "音乐放松",
                    1,
                    1,
                    0
                )
            )
            videoDataDao.insertAll(
                VideoData(
                    0,
                    "轻音乐",
                    "优雅悠扬的轻音乐为您的日常生活增添轻松愉快，提升情绪，提高工作效率。",
                    "${video_path}轻音乐.mp4",
                    "音乐放松",
                    1,
                    1,
                    0
                )
            )

            videoDataDao.insertAll(
                VideoData(
                    0,
                    "大海白噪音",
                    "大海白噪音以海浪声助您缓解压力，改善睡眠质量，提高专注力。",
                    "${video_path}大海白噪音.mp4",
                    "白噪音放松",
                    1,
                    1,
                    0
                )
            )
            videoDataDao.insertAll(
                VideoData(
                    0,
                    "森林白噪音",
                    "森林白噪音用自然声音帮助您深度放松，冥想，走进静谧安宁的心灵世界。",
                    "${video_path}森林白噪音.mp4",
                    "白噪音放松",
                    1,
                    1,
                    0
                )
            )
            videoDataDao.insertAll(
                VideoData(
                    0,
                    "雨声白噪音",
                    "雨声白噪音以诗意的雨滴声助您改善睡眠，减轻压力，达到内心的平静状态。",
                    "${video_path}雨声白噪音.mp4",
                    "白噪音放松",
                    1,
                    1,
                    0
                )
            )

            videoDataDao.insertAll(
                VideoData(
                    0,
                    "腹式呼吸放松训练",
                    "腹式呼吸训练视频指导您正确呼吸，帮助您深度放松，缓解压力，提升整体健康状况。",
                    "${video_path}腹式呼吸放松训练.mp4",
                    "指导放松",
                    1,
                    1,
                    0
                )
            )
            videoDataDao.insertAll(
                VideoData(
                    0,
                    "渐进式肌肉放松训练",
                    "渐进式肌肉放松训练指导您逐一放松身体肌肉，帮助您释放紧张，恢复身心平衡。",
                    "${video_path}渐进式肌肉放松训练.mp4",
                    "指导放松",
                    1,
                    1,
                    0
                )
            )
            videoDataDao.insertAll(
                VideoData(
                    0,
                    "意象放松训练",
                    "意象放松训练引导您运用想象力，缓解压力，提升心理健康，享受内心的宁静。",
                    "${video_path}意象放松训练.mp4",
                    "指导放松",
                    1,
                    1,
                    0
                )
            )


            videoDataDao.insertAll(
                VideoData(
                    0,
                    "正念减压身体扫描训练",
                    "通过这段视频的正念身体扫描，助您缓解压力，增强身心意识。",
                    "${video_path}正念减压身体扫描训练.mp4",
                    "正念冥想",
                    1,
                    2,
                    0
                )
            )
            videoDataDao.insertAll(
                VideoData(
                    0,
                    "正念呼吸训练",
                    "此视频引导您进行正念呼吸，帮助深度放松，提升生活质量和心理健康。",
                    "${video_path}正念呼吸训练.mp4",
                    "正念冥想",
                    1,
                    2,
                    0
                )
            )
            videoDataDao.insertAll(
                VideoData(
                    0,
                    "正念积极自我肯定训练",
                    "此视频将引导您进行自我肯定，通过正念训练，提升自尊和自信。",
                    "${video_path}正念积极自我肯定训练.mp4",
                    "正念冥想",
                    1,
                    2,
                    0
                )
            )
            videoDataDao.insertAll(
                VideoData(
                    0,
                    "正念按摩深度放松训练",
                    "此视频教导您如何利用正念按摩来达到身心的深度放松，释放压力。",
                    "${video_path}正念按摩深度放松训练.mp4",
                    "正念冥想",
                    1,
                    2,
                    0
                )
            )

            videoDataDao.insertAll(
                VideoData(
                    0,
                    "专注呼吸冥想练习",
                    "此冥想练习引导您专注于呼吸，提升注意力，促进内心平静。",
                    "${video_path}专注呼吸冥想练习.mp4",
                    "专注冥想",
                    1,
                    2,
                    0
                )
            )
            videoDataDao.insertAll(
                VideoData(
                    0,
                    "专注目标冥想练习",
                    "此视频引导您通过冥想专注于特定的目标，提高集中力和目标达成率。",
                    "${video_path}专注目标冥想练习.mp4",
                    "专注冥想",
                    1,
                    2,
                    0
                )
            )
            videoDataDao.insertAll(
                VideoData(
                    0,
                    "考前专注冥想练习",
                    "这是一段考前冥想训练，帮助您提高专注力，缓解考试焦虑。",
                    "${video_path}考前专注冥想练习.mp4",
                    "专注冥想",
                    1,
                    2,
                    0
                )
            )

            videoDataDao.insertAll(
                VideoData(
                    0,
                    "做自己生命中的父母",
                    "此视频引导您充当自己生命中的父母角色，提升自我接纳，培养自尊和自爱。",
                    "${video_path}做自己生命中的父母.mp4",
                    "内观冥想",
                    1,
                    2,
                    0
                )
            )
            videoDataDao.insertAll(
                VideoData(
                    0,
                    "与内在小孩接触",
                    "这个视频将提升您的自我理解和接纳，更好地处理情绪。",
                    "${video_path}与内在小孩接触.mp4",
                    "内观冥想",
                    1,
                    2,
                    0
                )
            )
            videoDataDao.insertAll(
                VideoData(
                    0,
                    "与生命智慧的老人相遇",
                    "此视频引导您去认真倾听自己内心的声音，挖掘内在智慧，提升生活质量。",
                    "${video_path}与生命智慧的老人相遇.mp4",
                    "内观冥想",
                    1,
                    2,
                    0
                )
            )

            videoDataDao.insertAll(
                VideoData(
                    0,
                    "慈悲祝福冥想",
                    "以慈悲祝福的冥想练习，帮助您培养同情心，提升人际关系。",
                    "${video_path}慈悲祝福冥想.mp4",
                    "慈悲冥想",
                    1,
                    2,
                    0
                )
            )
            videoDataDao.insertAll(
                VideoData(
                    0,
                    "慈悲接纳冥想训练",
                    "这个视频引导您通过冥想接纳自我和他人，提升包容力和人生满足感。",
                    "${video_path}慈悲接纳冥想训练.mp4",
                    "慈悲冥想",
                    1,
                    2,
                    0
                )
            )
            videoDataDao.insertAll(
                VideoData(
                    0,
                    "慈心冥想",
                    "慈心冥想训练帮助您发掘内在的善良，提升爱心和宽恕能力。",
                    "${video_path}慈心冥想.mp4",
                    "慈悲冥想",
                    1,
                    2,
                    0
                )
            )
        }.start()

    }


    /**
     * 绑定设备
     */
    private fun setDevice() {
        val uniqueDeviceId = DeviceUtils.getUniqueDeviceId()
        Log.e("IDID",uniqueDeviceId)

        /**
         * 绑定设备ID 非此设备ID不能使用
         */
        val list = mutableListOf<String>()
        list.add("287361b4859dc3fb5820afe6fafe4daa3")
        list.add("25471cca84f2d37278b67fbde1344bab6")
        list.add("2b187652bb51f3ba7a3d2f0388f183fd1")
        list.add("22973595ca3f531ddaa354e0acfb86fa1")
        list.add("2bd2cf30eed0d36b4bb44ffa2f59272c6")
        var isDevice = false
        list.forEach {
            if (it == uniqueDeviceId) {
                isDevice = true
            }
        }
        if (!isDevice) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("错误")
            builder.setMessage("此设备未绑定，即将退出")
            builder.setPositiveButton("我知道了") { _, _ ->
                AppUtils.exitApp()
            }
            builder.setCancelable(false)
            val dialog = builder.create()
            dialog.show()
        }
    }


    override fun initVariableId() = BR.login


    private val REQUEST_CODE: Int = 0x01
    private lateinit var mBle: Ble<BleDevice>

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

    var uuid1 = "438789fb-2849-42a0-9029-8c35a9f60097"
    var uuid2 = "438789fc-2849-42a0-9029-8c35a9f60097"

    //初始化蓝牙
    private fun initBLE() {
        mBle = Ble.options().apply {
            logBleEnable = true
            throwBleException = true
            autoConnect = true
            connectFailedRetryCount = 0
            connectTimeout = 10000L
            scanPeriod = 12000L
            uuidService = UUID.fromString(uuid1)
            uuidWriteCha = UUID.fromString(uuid2)
            bleWrapperCallback = MyBleWrapperCallback()
        }.create(applicationContext, object : Ble.InitCallback {
            override fun failed(failedCode: Int) {
                Log.e(TAG, "init failed: $failedCode")
            }

            override fun success() {
                Log.e(TAG, "init success")
            }

        })
        //3、检查蓝牙是否支持及打开
        checkBluetoothStatus()
    }

    //检查蓝牙是否支持及打开
    @SuppressLint("MissingPermission")
    private fun checkBluetoothStatus() {
        // 检查设备是否支持BLE4.0
        if (!mBle.isSupportBle(this)) {
            finish()
        }
        if (!mBle.isBleEnable) {
            //4、若未打开，则请求打开蓝牙
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, Ble.REQUEST_ENABLE_BT)
        } else {
            //5、若已打开，则进行扫描
            mBle.startScan(bleScanCallback())
        }
    }

    private fun bleScanCallback(): BleScanCallback<BleDevice> {
        return object : BleScanCallback<BleDevice>() {
            override fun onLeScan(device: BleDevice?, rssi: Int, scanRecord: ByteArray?) {
                Log.e(TAG, "${device?.bleName}")
                if (TextUtils.isEmpty(device?.bleName)) {
                    return
                }
                device?.let {
                    if (device.bleName.contains("YJ-AMY")) {
                        mBle.connect(device, connectCallback())
                        mBle.stopScan()
                    }
                }
            }
        }
    }

    private fun connectCallback(): BleConnectCallback<BleDevice> {
        return object : BleConnectCallback<BleDevice>() {
            override fun onConnectionChanged(device: BleDevice?) {
                Log.e(TAG, "onConnectionChanged: 连接成功")
            }

            override fun onConnectCancel(device: BleDevice?) {
                super.onConnectCancel(device)
                Log.e(TAG, "onConnectCancel: ")
            }

            override fun onConnectFailed(device: BleDevice?, errorCode: Int) {
                super.onConnectFailed(device, errorCode)
                Log.e(TAG, "连接异常，异常状态码:$errorCode")
            }

            override fun onReady(device: BleDevice?) {
                super.onReady(device)
                mBle.enableNotify(device, true, bleNotifyCallback())
                Log.e(TAG, "onReady: 就绪")
                Thread {
                    Thread.sleep(100)
                    runOnUiThread {
                        if (mBle.connectedDevices.size > 0)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                //此处第二个参数  不是特定的   比如你也可以设置500   但是如果设备不支持500个字节则会返回最大支持数
                                mBle.setMTU(
                                    mBle.connectedDevices[0].bleAddress,
                                    500,
                                    object : BleMtuCallback<BleDevice>() {
                                        override fun onMtuChanged(
                                            device: BleDevice,
                                            mtu: Int,
                                            status: Int
                                        ) {
                                            super.onMtuChanged(device, mtu, status)
                                            Log.e(TAG, "设置MTU：$mtu")
                                        }
                                    })
                            } else {
                                Log.e(TAG, "设备不支持MTU：")
                            }
                    }
                }.start()
            }

        }
    }

    private fun bleNotifyCallback(): BleNotifyCallback<BleDevice> {
        return object : BleNotifyCallback<BleDevice>() {
            override fun onChanged(
                device: BleDevice?,
                characteristic: BluetoothGattCharacteristic?
            ) {
                BleLog.i(
                    "收到硬件数据>>>>>onChanged:",
                    ByteUtils.toHexString(characteristic?.value)
                )
            }

            override fun onNotifySuccess(device: BleDevice?) {
                super.onNotifySuccess(device)
                BleLog.i(TAG, "设置通知成功:" + device?.bleName)
            }
        }
    }
}





