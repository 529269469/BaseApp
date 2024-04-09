package com.example.psychology.ui.fragment;

import static com.blankj.utilcode.util.SPStaticUtils.getBoolean;
import static com.example.psychology.ui.HomeJavcActivity._bIsConnectDevice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.room.Room;

import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.cusoft.android.bledevice.util.PublicPrintLog;
import com.example.psychology.R;
import com.example.psychology.base_app.utils.CreationChart;
import com.example.psychology.base_app.utils.VideoPlayerUtils;
import com.example.psychology.bean.DeleteEvent;
import com.example.psychology.bean.HrvEvent;
import com.example.psychology.bean.MessageEvent;
import com.example.psychology.bean.PlayEvent;
import com.example.psychology.ble.BleUtils;
import com.example.psychology.romm.base.AppDatabase;
import com.example.psychology.romm.dao.BrainDao;
import com.example.psychology.romm.dao.CalendarDao;
import com.example.psychology.romm.dao.RadarChartDao;
import com.example.psychology.romm.dao.ReportDataDao;
import com.example.psychology.romm.dao.UserDao;
import com.example.psychology.romm.dao.VideoDataDao;
import com.example.psychology.romm.entity.BrainData;
import com.example.psychology.romm.entity.CalendarData;
import com.example.psychology.romm.entity.RadarChartData;
import com.example.psychology.romm.entity.ReportData;
import com.example.psychology.romm.entity.User;
import com.example.psychology.romm.entity.VideoData;
import com.example.psychology.ui.HomeJavcActivity;
import com.example.psychology.ui.LoginActivity;
import com.example.psychology.ui.PlayActivity;
import com.example.psychology.ui.my.MyActivity;
import com.example.psychology.ui.view.IsPlayPopupWindow;
import com.example.psychology.ui.view.LoadingDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.king.wyk.fitnesstracker.Ble.BleCallback;
import com.king.wyk.fitnesstracker.Ble.BleHelper;
import com.king.wyk.fitnesstracker.Callback;
import com.king.wyk.fitnesstracker.FitnessTrackerModel;
import com.king.wyk.fitnesstracker.ResponseData.BodyStateData;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TrainingFragment2 extends Fragment {
    private SurfaceView pvTraining;
    private Button btTraining1, btTraining2;
    private ImageView iv_training_favorite, iv_training_history, iv_line_chart_close, iv_training_play, iv_training_stop, iv_training_full, iv_training, ivTrainingLeft;
    private LineChart chart_training;
    private LinearLayout ll_chart_training;
    private RelativeLayout rv_line_chart_close, ll_training_play;
    private long id;
    private VideoData videoData;

    private int time = 0;
    private String TAG = "TAG";

    private TextView tv_training_time, tv_training_time2, tvTrainingLeft;

    private SeekBar pb_training_progress;

    public static int drillType;
    private SurfaceHolder holder;
    private String video_path;
    private long reportId;

    public static boolean isplay = false;
    private IsPlayPopupWindow isPlayPopupWindow;


    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training2, null);
        Bundle arguments = getArguments();
        id = arguments.getLong("id");
        video_path = arguments.getString("video_path");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        reportId = System.currentTimeMillis();
        iv_line_chart_close = view.findViewById(R.id.iv_line_chart_close);
        rv_line_chart_close = view.findViewById(R.id.rv_line_chart_close);
        pvTraining = view.findViewById(R.id.pv_training);
        btTraining1 = view.findViewById(R.id.bt_training_1);
        btTraining2 = view.findViewById(R.id.bt_training_2);
        chart_training = view.findViewById(R.id.chart_training);
        ll_chart_training = view.findViewById(R.id.ll_chart_training);
        iv_training_favorite = view.findViewById(R.id.iv_training_favorite);
        iv_training_history = view.findViewById(R.id.iv_training_history);
        iv_training_play = view.findViewById(R.id.iv_training_play);
        iv_training_stop = view.findViewById(R.id.iv_training_stop);
        pb_training_progress = view.findViewById(R.id.pb_training_progress);
        tv_training_time = view.findViewById(R.id.tv_training_time);
        tv_training_time2 = view.findViewById(R.id.tv_training_time2);
        iv_training_full = view.findViewById(R.id.iv_training_full);
        ll_training_play = view.findViewById(R.id.ll_training_play);
        iv_training = view.findViewById(R.id.iv_training);
        ivTrainingLeft = view.findViewById(R.id.ivTrainingLeft);
        tvTrainingLeft = view.findViewById(R.id.tvTrainingLeft);


        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(
                    requireActivity(),
                    AppDatabase.class, "users_dp"
            ).build();
            VideoDataDao videoDataDao = db.videoDataDao();
            videoData = videoDataDao.loadByIds(id);
            videoData.setNum(videoData.getNum() + 1);
            videoDataDao.update(videoData);

            CalendarDao calendarDao = db.calendarDao();
            List<CalendarData> byCatalogue = calendarDao.findByCatalogue(id, 2, LoginActivity.Companion.getUid());
            getActivity().runOnUiThread(() -> {
                tvTrainingLeft.setText(videoData.getVideo_title());
                String video_path = SDCardUtils.getSDCardPathByEnvironment() + "/Movies/生理反馈/img/" + videoData.getVideo_title() + ".png";
                Glide.with(getActivity())
                        .load(video_path)
                        .into(iv_training);
                if (byCatalogue.isEmpty()) {
                    iv_training_favorite.setImageResource(R.mipmap.icon_collect3);
                } else {
                    iv_training_favorite.setImageResource(R.mipmap.icon_collect4);
                }
            });
        }).start();

        Log.e("TAG", "onCreateView: " + id);

        holder = pvTraining.getHolder();
        setPlay();
        setLineData();


        btTraining1.setOnClickListener(v -> {
            drillType = 1;
            pb_training_progress.setOnTouchListener((v1, event) -> false);
            freeTraining("自由训练不会训练生成报告", 1);

        });
        btTraining2.setOnClickListener(v -> {
            if (_bIsConnectDevice) {
                number = 0;
                reportId = System.currentTimeMillis();
                drillType = 2;
                pb_training_progress.setOnTouchListener((v1, event) -> true);
                freeTraining("正式训练满六分钟可以得到训练报告", 2);
            } else {
                freeTraining("请确认脑电设备已经打开且正常运行", 4);
            }


        });

        ivTrainingLeft.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });

        iv_training_history.setOnClickListener(v -> {
            if (TrainingFragment2.isplay) {
                isPlayPopupWindow = new IsPlayPopupWindow(getActivity(), iv_training_history, "视频正在播放,退出训练后您的数据\n将不会保留");
                isPlayPopupWindow.setOnClickListener(() -> {
                    startActivity(new Intent(getActivity(), MyActivity.class).putExtra("type", 0));
                });
            } else {
                startActivity(new Intent(getActivity(), MyActivity.class).putExtra("type", 0));
            }

        });
        rv_line_chart_close.setOnClickListener(v -> {
            if (ll_chart_training.getVisibility() == View.GONE) {
                ll_chart_training.setVisibility(View.VISIBLE);
                iv_line_chart_close.setImageResource(R.mipmap.icon_line_chart_close);
            } else {
                ll_chart_training.setVisibility(View.GONE);
                iv_line_chart_close.setImageResource(R.mipmap.icon_line_chart_open);
            }


        });

        iv_training_play.setOnClickListener(v -> {
            if (drillType == 0 || drillType == 2) {
                return;
            }
            iv_training.setVisibility(View.GONE);
            isplay = true;
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    iv_training_play.setImageResource(R.mipmap.icon_training_play);
                } else {
                    iv_training_play.setImageResource(R.mipmap.icon_pause3);
                    mediaPlayer.setDisplay(holder);
                    mediaPlayer.start();
                }
            } else {
                setPlay();
                iv_training_play.setImageResource(R.mipmap.icon_pause3);
                mediaPlayer.setDisplay(holder);
                mediaPlayer.start();
                mediaPlayer.pause();
                mediaPlayer.start();
            }
            btTraining1.setVisibility(View.GONE);
            btTraining2.setVisibility(View.GONE);
        });

        iv_training_stop.setOnClickListener(v -> {
            if (drillType == 0) {
                return;
            }
            if (drillType == 2) {
                if (mediaPlayer != null) {
                    int current = mediaPlayer.getCurrentPosition() / 1000;
                    if (current > 360) {
                        stopPopup();
                    } else {
                        freeTraining("正式训练未满六分钟，未能生成报告", 3);
                    }
                }
            } else {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(0);
                    mediaPlayer.pause();
                    iv_training_play.setImageResource(R.mipmap.icon_training_play);
                }
                drillType = 0;
                btTraining1.setVisibility(View.VISIBLE);
                btTraining2.setVisibility(View.VISIBLE);
                iv_training.setVisibility(View.VISIBLE);
                isplay = false;
//                Navigation.findNavController(iv_training_stop).navigateUp();
            }

        });


        iv_training_full.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                Intent intent = new Intent(getActivity(), PlayActivity.class)
                        .putExtra("id", id)
                        .putExtra("video_path", videoData.getVideo_path())
                        .putExtra("number", number)
                        .putExtra("reportId", reportId)
                        .putExtra("seekTo", mediaPlayer.getCurrentPosition());
                getActivity().startActivity(intent);
            }
        });

        pvTraining.setOnClickListener(v -> {

        });

        pb_training_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (drillType == 1) {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.seekTo(seekBar.getProgress());
                    }
                }
            }
        });

        iv_training_favorite.setOnClickListener(v -> {
            Date nowDate = TimeUtils.getNowDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(nowDate);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int dayNum = year * 24 + month * 31 + day;
            new Thread(() -> {
                AppDatabase db = Room.databaseBuilder(
                        requireActivity(),
                        AppDatabase.class, "users_dp"
                ).build();
                CalendarDao calendarDao = db.calendarDao();
                List<CalendarData> byCatalogue = calendarDao.findByCatalogue(id, 2, LoginActivity.Companion.getUid());
                if (byCatalogue.isEmpty()) {
                    calendarDao.insertAll(new CalendarData(year, month, day, time, LoginActivity.Companion.getUid(), videoData.getCustom(), videoData.getVideo_title(), videoData.getVideo_path(), 2, id, dayNum, 1));
                    getActivity().runOnUiThread(() -> {
                        iv_training_favorite.setImageResource(R.mipmap.icon_collect4);
                    });
                } else {
                    calendarDao.delete(byCatalogue.get(0));
                    getActivity().runOnUiThread(() -> {
                        iv_training_favorite.setImageResource(R.mipmap.icon_collect3);
                    });
                }

            }).start();
        });

        return view;
    }


    private PopupWindow stopWindow;

    private int istop = 1;

    private void stopPopup() {
        if (stopWindow == null) {
            stopWindow = new PopupWindow(getActivity());
        }
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.layout_popup_stop, null);
        stopWindow.setContentView(inflate);
        stopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        stopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        stopWindow.setBackgroundDrawable(new ColorDrawable(0));
        stopWindow.setFocusable(true);
        stopWindow.setOutsideTouchable(true);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
        stopWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = getActivity().getWindow().getAttributes();
            lp1.alpha = 1f;
            getActivity().getWindow().setAttributes(lp1);
        });

        stopWindow.showAtLocation(btTraining1, Gravity.CENTER, 0, 0);
        inflate.findViewById(R.id.iv_stop_popup_x)
                .setOnClickListener(v -> {
//                    if (mediaPlayer != null) {
//                        mediaPlayer.setDisplay(holder);
//                        mediaPlayer.start();
//                        iv_training.setVisibility(View.GONE);
//                        isplay = true;
//                        iv_training_play.setImageResource(R.mipmap.icon_pause3);
//                    }
                    stopWindow.dismiss();
                });

        inflate.findViewById(R.id.bt_stop_yes)
                .setOnClickListener(v -> {
                    stopWindow.dismiss();
                    mediaPlayer.stop();
                    iv_training_play.setImageResource(R.mipmap.icon_training_play);
                    istop = 1;
                    setSave();

                });

        inflate.findViewById(R.id.bt_stop_no)
                .setOnClickListener(v -> {
                    stopWindow.dismiss();
                    mediaPlayer.pause();
                    iv_training_play.setImageResource(R.mipmap.icon_training_play);
                    istop = 2;
                    setSave();
                    isplay = false;
                });

    }

    LoadingDialog loadingDialog;

    private void setSave() {
        loadingDialog = new LoadingDialog(requireActivity(), iv_training_play);//显示
//        setEvent();
        getData();
    }

    private boolean isTimer = true;

    @SuppressLint("NewApi")
    public void getData() {
        AppDatabase db = Room.databaseBuilder(
                requireActivity(),
                AppDatabase.class, "users_dp"
        ).build();

        isTimer = true;
        HomeJavcActivity.fitnessTracker.getBodyStateCommand(new Callback<BodyStateData[]>() {
            @Override
            public void onSuccess(BodyStateData[] result) {
                if (result != null) {
                    Log.e(TAG, "手环: " + result.length);
                    for (int i = 0; i < result.length; i++) {
                        String ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(result[i].getTimestamp());
                        Log.e(TAG, "手环心率: " + result[i].getHeartRate()
                                + "  hrv: " + result[i].getHrv()
                                + "  血氧: " + result[i].getBloodOxygen()
                                + "  压力: " + result[i].getStress()
                                + "  情绪: " + result[i].getEmotion()
                                + "  疲劳: " + result[i].getFatigue()
                                + "  时间: " + ymdhms);
                        if (result[i].getHeartRate() != 0 || result[i].getHrv() != 0) {
                            int finalI = i;
                            new Thread(() -> {
                                RadarChartDao radarChartDao = db.radarChartDao();

                                float Stress = 0f;//压力
                                float Emotion = 0f;//情绪
                                float Fatigue = 0f;//疲劳
                                if (result[finalI].getStress() > 65f) {
                                    Stress = 65f;
                                }
                                if (result[finalI].getStress() < 25f) {
                                    Stress = 25f;
                                }
                                if (result[finalI].getEmotion() > 2.5f) {
                                    Emotion = 2.5f;
                                }
                                if (result[finalI].getEmotion() < 0.6f) {
                                    Emotion = 0.6f;
                                }
                                if (result[finalI].getFatigue() > 11.85f) {
                                    Fatigue = 11.85f;
                                }
                                if (result[finalI].getFatigue() < 8.62f) {
                                    Fatigue = 8.62f;
                                }

                                radarChartDao.insertAll(
                                        new RadarChartData(reportId, 3, finalI, 0, 100 - Stress),
                                        new RadarChartData(reportId, 4, finalI, 0, 100 - Emotion),
                                        new RadarChartData(reportId, 5, finalI, 0, 100 - Fatigue),
                                        new RadarChartData(reportId, 6, finalI, result[finalI].getHeartRate(), 0f),
                                        new RadarChartData(reportId, 7, finalI, result[finalI].getHrv(), 0f),
                                        new RadarChartData(reportId, 8, finalI, result[finalI].getBloodOxygen(), 0f));
                            }).start();
                        }

                    }
                    HomeJavcActivity.fitnessTracker.clearDataCommand();
                }
                if (isTimer) {
                    setEvent();
                }

            }

            @Override
            public void onFailed(String error) {
                Log.e(TAG, "onFailed: " + error);
            }
        });

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (isTimer) {
                    setEvent();
                }
            }
        };
        timer.schedule(timerTask, 10000);

    }

    private void setEvent() {
        isTimer = false;
        AppDatabase db = Room.databaseBuilder(
                requireActivity(),
                AppDatabase.class, "users_dp"
        ).build();
        new Thread(() -> {
            RadarChartDao radarChartDao = db.radarChartDao();
            ReportDataDao reportDataDao = db.ReportDataDao();
            List<RadarChartData> byMonth1 = radarChartDao.findByMonth(reportId, 1);
            List<RadarChartData> byMonth2 = radarChartDao.findByMonth(reportId, 2);
            List<RadarChartData> byMonth3 = radarChartDao.findByMonth(reportId, 3);

            List<RadarChartData> byMonth4 = radarChartDao.findByMonth(reportId, 4);
            List<RadarChartData> byMonth5 = radarChartDao.findByMonth(reportId, 5);
            Collections.reverse(byMonth3);

            if (byMonth1.isEmpty() || byMonth2.isEmpty() || byMonth1.size() < 60) {
                return;
            }


            List<RadarChartData> radar1_front = byMonth1.subList(0, 60);
            List<RadarChartData> radar1_centre = byMonth1.subList(60, byMonth1.size() - 60);
            List<RadarChartData> radar1_later = byMonth1.subList(byMonth1.size() - 60, byMonth1.size());

            List<RadarChartData> radar2_front = byMonth2.subList(0, 60);
            List<RadarChartData> radar2_centre = byMonth2.subList(60, byMonth1.size() - 60);
            List<RadarChartData> radar2_later = byMonth2.subList(byMonth1.size() - 60, byMonth1.size());

            List<RadarChartData> radar3_front = new ArrayList<>();
            List<RadarChartData> radar3_centre = new ArrayList<>();
            List<RadarChartData> radar3_later = new ArrayList<>();
            List<RadarChartData> radar4_front = new ArrayList<>();
            List<RadarChartData> radar4_centre = new ArrayList<>();
            List<RadarChartData> radar4_later = new ArrayList<>();
            List<RadarChartData> radar5_front = new ArrayList<>();
            List<RadarChartData> radar5_centre = new ArrayList<>();
            List<RadarChartData> radar5_later = new ArrayList<>();


            if (!byMonth3.isEmpty() && byMonth3.size() > 2) {
                radar3_front.addAll(byMonth3.subList(0, 1));
                radar3_centre.addAll(byMonth3.subList(1, byMonth3.size() - 1));
                radar3_later.addAll(byMonth3.subList(byMonth3.size() - 1, byMonth3.size()));
                radar4_front.addAll(byMonth4.subList(0, 1));
                radar4_centre.addAll(byMonth4.subList(1, byMonth4.size() - 1));
                radar4_later.addAll(byMonth4.subList(byMonth4.size() - 1, byMonth4.size()));
                radar5_front.addAll(byMonth5.subList(0, 1));
                radar5_centre.addAll(byMonth5.subList(1, byMonth5.size() - 1));
                radar5_later.addAll(byMonth5.subList(byMonth5.size() - 1, byMonth5.size()));

            }

            int zhuanzhu_a = 48;
            float zhuanzhu_b = 12f;
            int fangsong_a = 54;
            float fangsong_b = 12.5f;
            int yali_a = 47;
            float yali_b = 24.5f;
            int qingxu_a = 98;
            float qingxu_b = 1.21f;
            int pilao_a = 91;
            float pilao_b = 2.8f;
            /**
             * 专注
             */
            int[] zhuanzhu_front = getSum(radar1_front, zhuanzhu_a, zhuanzhu_b);
            int[] zhuanzhu_centre = getSum(radar1_centre, zhuanzhu_a, zhuanzhu_b);
            int[] zhuanzhu_later = getSum(radar1_later, zhuanzhu_a, zhuanzhu_b);

            /**
             * 放松
             */
            int[] fangsong_front = getSum(radar2_front, fangsong_a, fangsong_b);
            int[] fangsong_centre = getSum(radar2_centre, fangsong_a, fangsong_b);
            int[] fangsong_later = getSum(radar2_later, fangsong_a, fangsong_b);

            /**
             * 压力
             */
            int[] yali_front = getSum2(radar3_front, yali_a, yali_b, "压力前", byMonth3);
            int[] yali_centre = getSum2(radar3_centre, yali_a, yali_b, "压力中", byMonth3);
            int[] yali_later = getSum2(radar3_later, yali_a, yali_b, "压力后", byMonth3);

            /**
             * 情绪
             */
            int[] qingxu_front = getSum2(radar4_front, qingxu_a, qingxu_b, "情绪前", byMonth4);
            int[] qingxu_centre = getSum2(radar4_centre, qingxu_a, qingxu_b, "情绪中", byMonth4);
            int[] qingxu_later = getSum2(radar4_later, qingxu_a, qingxu_b, "情绪后", byMonth4);


            /**
             * 疲劳
             */

            int[] pilao_front = getSum2(radar5_front, pilao_a, pilao_b, "疲劳前", byMonth5);
            int[] pilao_centre = getSum2(radar5_centre, pilao_a, pilao_b, "疲劳中", byMonth5);
            int[] pilao_later = getSum2(radar5_later, pilao_a, pilao_b, "疲劳后", byMonth5);

            Date nowDate = TimeUtils.getNowDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(nowDate);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            int zhuanzhu_effectiveness = getLadar(zhuanzhu_front, zhuanzhu_later, 6f, 10f);
            int fangsong_effectiveness = getLadar(fangsong_front, fangsong_later, 5f, 9f);
            int yali_effectiveness = getLadar(yali_front, yali_later, 1f, 8f);
            int qingxu_effectiveness = getLadar(qingxu_front, qingxu_later, 2f, 3f);
            int pilao_effectiveness = getLadar(pilao_front, pilao_later, 6.4f, 10f);
            if (yali_effectiveness>90){
                yali_effectiveness=90;
            }
            if (yali_effectiveness<20){
                yali_effectiveness=20;
            }

            if (qingxu_effectiveness>90){
                qingxu_effectiveness=90;
            }
            if (qingxu_effectiveness<20){
                qingxu_effectiveness=20;
            }

            if (pilao_effectiveness>90){
                pilao_effectiveness=90;
            }
            if (pilao_effectiveness<20){
                pilao_effectiveness=20;
            }

//            int report_dirll_score = (int) ((zhuanzhu_centre[0] * 0.4) + (fangsong_centre[0] * 0.6));
//            int report_mind_score = (int) ((zhuanzhu_effectiveness * 0.4) + (fangsong_effectiveness * 0.6));


            int report_dirll_score = (int) ((zhuanzhu_centre[0] * 0.3) + (fangsong_centre[0] * 0.4) + (qingxu_centre[0] * 0.1) + (yali_centre[0] * 0.1) + (pilao_centre[0] * 0.1));
            int report_mind_score = (int) ((zhuanzhu_effectiveness * 0.3) + (fangsong_effectiveness * 0.4) + (qingxu_effectiveness * 0.1) + (yali_effectiveness * 0.1) + (pilao_effectiveness * 0.1));

            reportDataDao.insertAll(new ReportData(reportId,
                    LoginActivity.Companion.getUid(),
                    year,
                    month,
                    day,
                    report_dirll_score,
                    report_mind_score,
                    "",
                    "",
                    "",
                    "",
                    zhuanzhu_front[0],
                    zhuanzhu_centre[0],
                    zhuanzhu_later[0],
                    zhuanzhu_front[1],
                    zhuanzhu_centre[1],
                    zhuanzhu_later[1],
                    zhuanzhu_front[2],
                    zhuanzhu_centre[2],
                    zhuanzhu_later[2],
                    zhuanzhu_effectiveness,
                    fangsong_front[0],
                    fangsong_centre[0],
                    fangsong_later[0],
                    fangsong_front[1],
                    fangsong_centre[1],
                    fangsong_later[1],
                    fangsong_front[2],
                    fangsong_centre[2],
                    fangsong_later[2],
                    fangsong_effectiveness,
                    yali_front[0],
                    yali_centre[0],
                    yali_later[0],
                    yali_front[1],
                    yali_centre[1],
                    yali_later[1],
                    yali_front[2],
                    yali_centre[2],
                    yali_later[2],
                    yali_effectiveness,
                    qingxu_front[0],
                    qingxu_centre[0],
                    qingxu_later[0],
                    qingxu_front[1],
                    qingxu_centre[1],
                    qingxu_later[1],
                    qingxu_front[2],
                    qingxu_centre[2],
                    qingxu_later[2],
                    qingxu_effectiveness,
                    pilao_front[0],
                    pilao_centre[0],
                    pilao_later[0],
                    pilao_front[1],
                    pilao_centre[1],
                    pilao_later[1],
                    pilao_front[2],
                    pilao_centre[2],
                    pilao_later[2],
                    pilao_effectiveness));
            getActivity().runOnUiThread(() -> {
                if (istop == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("id", reportId);
                    Navigation.findNavController(iv_training_stop)
                            .navigate(R.id.action_trainingFragment2_to_reportDetailsFragment, bundle);
                } else {
                    drillType = 0;
                    btTraining1.setVisibility(View.VISIBLE);
                    btTraining2.setVisibility(View.VISIBLE);
                    iv_training.setVisibility(View.VISIBLE);

                }
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                reportId = System.currentTimeMillis();
                stopWindow.dismiss();
            });
        }).start();
    }

    /**
     * @param front 前测
     * @param later 后测
     */
    private int getLadar(int[] front, int[] later, float sqrt, float sqrt2) {
        int i1 = later[0] - front[0];
        Log.e(TAG, ":getLadar: ____________________________");
        Log.e(TAG, ":getLadar: " + later[0] + "-" + front[0] + "=" + i1 + "  ");
//        double sqrt = Math.sqrt(i1 * i1 );
        double v = ((i1 - sqrt) / sqrt2) * 10 + 65;
        int i = new Double(v).intValue();
        if (i > 100) {
            i = 100;
        }
        return i;
    }


    /**
     * @param radarChartDataList 测值集合
     * @param average            常值
     * @param average2           开方值
     * @return
     */
    private int[] getSum(List<RadarChartData> radarChartDataList, int average, float average2) {
        if (radarChartDataList.isEmpty()) {
            int[] sum = {0, 0, 0};
            return sum;
        }
        int sumNum = 0;
        for (int i = 0; i < radarChartDataList.size(); i++) {
            sumNum = sumNum + radarChartDataList.get(i).getNum();
        }
        //平均值
        sumNum = sumNum / radarChartDataList.size();

        int differenceSum = 0;
        for (int i = 0; i < radarChartDataList.size(); i++) {
            Integer numThem = radarChartDataList.get(i).getNum();
            if (sumNum > numThem) {
                differenceSum = differenceSum + ((sumNum - numThem) * (sumNum - numThem));
            } else {
                differenceSum = differenceSum + ((numThem - sumNum) * (numThem - sumNum));
            }
        }
        differenceSum = differenceSum / radarChartDataList.size();

        //开方平均差值
        double sqrt = Math.sqrt(differenceSum);
        Log.e(TAG, "平均值: " + sumNum);
        Log.e(TAG, "开方平均差值: " + sqrt);
        Log.e(TAG, "average2: " + average2);
        float z = (float) (((sumNum - average) / average2) * 10 + 65);
        Log.e(TAG, "z: " + z);
        int[] sum = {(int) z, sumNum, (int) sqrt};
        return sum;
    }

    private int[] getSum2(List<RadarChartData> radarChartDataList, int average, float average2, String title, List<RadarChartData> byMonth) {
        if (radarChartDataList.isEmpty()) {
            int[] sum = {0, 0, 0};
            return sum;
        }
        float sumNum = 0;
        for (int i = 0; i < byMonth.size(); i++) {
            sumNum = sumNum + byMonth.get(i).getNum2();
        }
        //平均值
        sumNum = sumNum / byMonth.size();
        float sumNum2 = 0;
        float differenceSum = 0;
        for (int i = 0; i < radarChartDataList.size(); i++) {
            sumNum2 = sumNum2 + radarChartDataList.get(i).getNum2();
            float numThem = radarChartDataList.get(i).getNum2();
            differenceSum = differenceSum + ((sumNum - numThem) * (sumNum - numThem));
        }
        sumNum2 = sumNum2 / radarChartDataList.size();

        differenceSum = differenceSum / radarChartDataList.size();

        Log.e(TAG, title + ": " + differenceSum);
        //开方平均差值
        double sqrt = Math.sqrt(differenceSum);
        Log.e(TAG, title + ":平均值: " + sumNum + "-" + average + "   " + sumNum2 + ":开方平均差值: " + sqrt + ":average2: " + average2);
        int z = (int) (((sumNum2 - average) / average2) * 10 + 65);
        Log.e(TAG, "z: " + z);
        int sunz = Math.abs(z);
        int[] sum = {sunz, (int) sumNum2, (int) sqrt};
        return sum;
    }


    private CreationChart setChart;

    private void setLineData() {
        setChart = new CreationChart(chart_training);
        setChart.init();
    }


    private PopupWindow freeWindow;

    @SuppressLint("MissingInflatedId")
    private void freeTraining(String content, int type) {
        freeWindow = new PopupWindow(getActivity());
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.layout_popup_free, null);
        freeWindow.setContentView(inflate);
        freeWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        freeWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        freeWindow.setBackgroundDrawable(new ColorDrawable(0));
        freeWindow.setFocusable(true);
        freeWindow.setOutsideTouchable(true);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
        freeWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = getActivity().getWindow().getAttributes();
            lp1.alpha = 1f;
            getActivity().getWindow().setAttributes(lp1);
        });
        freeWindow.showAtLocation(btTraining1, Gravity.CENTER, 0, 0);
        inflate.findViewById(R.id.iv_free_popup_x)
                .setOnClickListener(v -> {
                    freeWindow.dismiss();
                });
        TextView tv_free_content = inflate.findViewById(R.id.tv_free_content);
        tv_free_content.setText(content);
        inflate.findViewById(R.id.bt_free_yes).setOnClickListener(v -> {
            switch (type) {
                case 1:
                    if (mediaPlayer == null) {
                        setPlay();
                    }
                    iv_training_play.setImageResource(R.mipmap.icon_pause3);
                    btTraining1.setVisibility(View.GONE);
                    btTraining2.setVisibility(View.GONE);
                    mediaPlayer.setDisplay(holder);
                    mediaPlayer.start();
                    mediaPlayer.pause();
                    mediaPlayer.start();
                    iv_training.setVisibility(View.GONE);
                    isplay = true;
                case 2:
                    new Thread(() -> {
                        try {
                            Thread.sleep(3000);
                            HomeJavcActivity.fitnessTracker.clearDataCommand();
                            Thread.sleep(3000);
                            HomeJavcActivity.fitnessTracker.setTimeCommand((int) (System.currentTimeMillis() / 1000));
                            Thread.sleep(3000);
                            HomeJavcActivity.fitnessTracker.setMonitorCommand(true);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
                    if (mediaPlayer == null) {
                        setPlay();
                    }
                    iv_training_play.setImageResource(R.mipmap.icon_pause3);
                    mediaPlayer.setDisplay(holder);
                    btTraining1.setVisibility(View.GONE);
                    btTraining2.setVisibility(View.GONE);
                    mediaPlayer.start();
                    mediaPlayer.pause();
                    mediaPlayer.start();
                    iv_training.setVisibility(View.GONE);
                    isplay = true;
                    break;
                case 3:
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;
                        btTraining1.setVisibility(View.VISIBLE);
                        btTraining2.setVisibility(View.VISIBLE);
                        iv_training_play.setImageResource(R.mipmap.icon_training_play);
                        iv_training.setVisibility(View.VISIBLE);
                        isplay = false;
                    }
                    break;
            }
            freeWindow.dismiss();

        });

    }

    public MediaPlayer mediaPlayer = null;

    private void setPlay() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        try {
            Log.e(TAG, "setPlay: " + video_path);
            mediaPlayer.setDataSource(requireActivity(), Uri.parse(video_path));
            mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(mp -> {
            mediaPlayer.setLooping(false);
        });
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(10086);
            }
        };
        timer.schedule(timerTask, 500, 1000);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (isPlayPopupWindow != null) {
            isPlayPopupWindow.dismiss();
            isPlayPopupWindow = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "执行了 onStop ");
        drillType = 0;
        AppDatabase db = Room.databaseBuilder(
                requireActivity(),
                AppDatabase.class, "users_dp"
        ).build();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (stopWindow != null) {
            stopWindow.dismiss();
        }
//        HomeJavcActivity.bleHelper.disconnect();
        isplay = false;
        Date nowDate = TimeUtils.getNowDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int dayNum = year * 24 + month * 31 + day;
        new Thread(() -> {
            if (time > 0) {
                CalendarDao calendarDao = db.calendarDao();
                calendarDao.insertAll(new CalendarData(year, month, day, time, LoginActivity.Companion.getUid(), videoData.getCustom(), videoData.getVideo_title(), videoData.getVideo_path(), 1, id, dayNum, 1));
            }
        }).start();
    }


    private int number = 0;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("MissingPermission")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10086:
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        time = mediaPlayer.getCurrentPosition();
                        int current = mediaPlayer.getCurrentPosition();
                        int duration = mediaPlayer.getDuration();
                        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
                        tv_training_time.setText(format.format(current));
                        tv_training_time2.setText(format.format(duration));
                        pb_training_progress.setMax(mediaPlayer.getDuration());
                        pb_training_progress.setProgress(mediaPlayer.getCurrentPosition());

                        if (current > (duration - 1000)) {
                            mediaPlayer.pause();
                            if (current / 1000 > 360) {
                                stopPopup();
                            } else {
                                freeTraining("正式训练未满六分钟，未能生成报告", 3);
                            }
                        }
                    }
                    break;
                case 10012:
                    if (mediaPlayer == null) {
                        setPlay();
                    }
                    holder.addCallback(new SurfaceHolder.Callback() {
                        @Override
                        public void surfaceCreated(@NonNull SurfaceHolder holder) {
                            mediaPlayer.setDisplay(holder);
                            mediaPlayer.seekTo(event.getSeekto());
                            mediaPlayer.start();
                            iv_training.setVisibility(View.GONE);
                            isplay = true;
                            iv_training_play.setImageResource(R.mipmap.icon_pause3);
                        }

                        @Override
                        public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

                        }

                        @Override
                        public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

                        }
                    });
                    break;
            }
        }
    };


    private DeleteEvent event;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeleteEvent event) {
        this.event = event;
        number = event.getNumber();
        if (event.isStop()) {
            iv_training_play.setImageResource(R.mipmap.icon_training_play);
            if (drillType == 2) {
                if (number > 360) {
                    stopPopup();
                } else {
                    freeTraining("正式训练未满六分钟，未能生成报告", 3);
                }
            } else {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(0);
                    mediaPlayer.pause();
                }
                btTraining1.setVisibility(View.VISIBLE);
                btTraining2.setVisibility(View.VISIBLE);
                iv_training.setVisibility(View.VISIBLE);
                isplay = false;
            }
        } else {
            handler.sendEmptyMessage(10012);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PlayEvent event) {
        if (mediaPlayer != null) {
            if (!event.isStop()) {
                mediaPlayer.pause();
                iv_training_play.setImageResource(R.mipmap.icon_training_play);
            } else {
                iv_training_play.setImageResource(R.mipmap.icon_pause3);
                mediaPlayer.setDisplay(holder);
                mediaPlayer.start();
            }
            if (event.getType() == 1) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(0);
                    mediaPlayer.pause();
                    iv_training_play.setImageResource(R.mipmap.icon_training_play);
                }
                drillType = 0;
                btTraining1.setVisibility(View.VISIBLE);
                btTraining2.setVisibility(View.VISIBLE);
                iv_training.setVisibility(View.VISIBLE);
                isplay = false;
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        int attention = event.getAttention();//专注
        int meditation = event.getMeditation();//放松
        if (attention == 0 || meditation == 0) {
            return;
        }
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            setChart.AddData(
                    (float) attention,
                    (float) meditation);
            AppDatabase db = Room.databaseBuilder(
                    requireActivity(),
                    AppDatabase.class, "users_dp"
            ).build();
            number++;
            new Thread(() -> {
                RadarChartDao radarChartDao = db.radarChartDao();
                radarChartDao.insertAll(new RadarChartData(reportId, 1, number, attention, 0f),
                        new RadarChartData(reportId, 2, number, meditation, 0f));

                BrainDao brainDao = db.brainDao();
                brainDao.insertAll(new BrainData(reportId, number, event.getDelta(),
                        event.getTheta(),
                        event.getLowAlpha(),
                        event.getHighAlpha(),
                        event.getLowBeta(),
                        event.getHighBeta(),
                        event.getLowGamma(),
                        event.getMidGamma()));
            }).start();
            Log.e("TAG", "handleMessage: 专注: " + attention + "  meditation: 放松:" + meditation + "   number:" + number + "   reportId:" + reportId);
        }

    }

}
