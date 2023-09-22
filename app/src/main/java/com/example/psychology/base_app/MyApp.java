package com.example.psychology.base_app;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;


import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.example.psychology.base_app.expand.ResourceExtKt;
import com.example.psychology.base_app.utils.base.apputils.AppManager;

import com.king.wyk.fitnesstracker.Log.Logger;
import com.tencent.mmkv.MMKV;

import rxhttp.RxHttpPlugins;

public class MyApp extends Application {

    private String TAG = "MyApp";

    @Override
    public void onCreate() {
        super.onCreate();
        // 设置初始化的根目录
        String dir = getFilesDir().getAbsolutePath() + "/mmkv_2";
        String rootDir = MMKV.initialize(dir);
//        Log.e(TAG, "mmkv root: " + rootDir);

        AppManager.Companion.getInstance().init(this);
        ResourceExtKt.isPrintLog(true);//是否打印log日志x
        RxHttpPlugins.init(RxHttpPlugins.getOkHttpClient()).setDebug(true);//rxhttp网络请求debug是否开启

        //日志初始化
        Logger.getInstance().setContext(this);
        String video_path = SDCardUtils.getSDCardPathByEnvironment() + "/Movies/生理反馈/log";
        LogUtils.getConfig()
                .setLogSwitch(true)
                .setLog2FileSwitch(true)
                .setDir(video_path);


    }
}
