package com.example.psychology.ble;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import com.example.psychology.bean.HrvEvent;
import com.example.psychology.romm.base.AppDatabase;
import com.example.psychology.romm.dao.RadarChartDao;
import com.example.psychology.romm.entity.RadarChartData;
import com.king.wyk.fitnesstracker.Ble.BleCallback;
import com.king.wyk.fitnesstracker.Ble.BleHelper;
import com.king.wyk.fitnesstracker.Callback;
import com.king.wyk.fitnesstracker.FitnessTrackerModel;
import com.king.wyk.fitnesstracker.ResponseData.BodyStateData;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class BleUtils {

    private final String TAG = "BleUtils";

    public BleHelper bleHelper;
    public FitnessTrackerModel fitnessTracker;


    int num;
    long reportId;
    AppDatabase db;

    public void bleInit(Activity activity, int num, long reportId, AppDatabase db) {
        this.num=num;
        this.reportId=reportId;
        this.db=db;

        bleHelper = new BleHelper(activity);
        //获取手环
        fitnessTracker = bleHelper.getFitnessTracker();
        //添加ble状态回调
        bleHelper.setBleCallback(new BleCallback() {
            @Override
            public void connectCallback() {
                Log.e(TAG, "setBleCallback:获取BodyData ");
                getData();

            }

            @Override
            public void disConnectCallback() {

            }

            @Override
            public void connectFailCallback(String s) {

            }

        });
        //连接
        bleHelper.connect();

    }

    @SuppressLint("NewApi")
    public void getData() {
        int time = (int) (System.currentTimeMillis() / 1000);
        Log.e(TAG, "time: " + time);
        fitnessTracker.setTimeCommand(time);
        fitnessTracker.getBodyStateCommand(new Callback<BodyStateData[]>() {
            @Override
            public void onSuccess(BodyStateData[] result) {
                Log.e(TAG, "onSuccess: ");
                if (result != null) {
                    Log.e(TAG, "getBodyStateCommand: " + result.length);

                    for (int i = result.length-num; i < result.length; i++) {
                        String ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(result[i].getTimestamp());
                        Log.e(TAG, "心率: " + result[i].getHeartRate()
                                + "  hrv: " + result[i].getHrv()
                                + "  血氧: " + result[i].getBloodOxygen()
                                + "  压力: " + result[i].getStress()
                                + "  情绪: " + result[i].getEmotion()
                                + "  疲劳: " + result[i].getFatigue()
                                + "  时间: " + ymdhms);
//                        if (result[i].getHeartRate() != 0 || result[i].getHrv() != 0) {
//                            int finalI = i;
//                            new Thread(() -> {
//                                RadarChartDao radarChartDao = db.radarChartDao();
//                                radarChartDao.insertAll(
//                                        new RadarChartData(reportId, 3,finalI, 0, result[finalI].getStress()),
//                                        new RadarChartData(reportId, 4,finalI, 0, result[finalI].getEmotion()),
//                                        new RadarChartData(reportId, 5,finalI, 0, result[finalI].getFatigue()),
//                                        new RadarChartData(reportId, 6,finalI, result[finalI].getHeartRate(), 0f),
//                                        new RadarChartData(reportId, 7,finalI, result[finalI].getHrv(), 0f),
//                                        new RadarChartData(reportId, 8,finalI, result[finalI].getBloodOxygen(), 0f));
//
//                                if (finalI==result.length-1){
//                                    EventBus.getDefault().post(new HrvEvent(0,0,
//                                            0,
//                                            0,
//                                            0,
//                                            0,
//                                            0L));
//                                }
//                            }).start();
//                        }


                    }
                    fitnessTracker.clearDataCommand();
                }
            }

            @Override
            public void onFailed(String error) {
                Log.e(TAG, "onFailed: " + error);
            }
        });

    }

    public void setClose() {
        Log.e(TAG, "setClose: ");
        fitnessTracker.clearDataCommand();
    }
}
