package com.example.psychology.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.cusoft.android.bledevice.control.BLEDevice;
import com.cusoft.android.bledevice.model.CusoftModel;
import com.cusoft.android.bledevice.util.CusoftConfig;
import com.cusoft.android.bledevice.util.CusoftUtil;
import com.cusoft.android.bledevice.util.PublicPrintLog;

import com.cusoft.android.egox.EgoXDevice;
import com.cusoft.android.egox.model.EgoxModel;
import com.cusoft.android.egox.util.EgoXConfig;
import com.example.psychology.R;
import com.example.psychology.base_app.utils.statusbar.StatusBar;
import com.example.psychology.bean.DeleteEvent;
import com.example.psychology.bean.MessageEvent;
import com.example.psychology.bean.PlayEvent;
import com.example.psychology.ble.BleUtils;
import com.example.psychology.databinding.ActivityHomeBinding;
import com.example.psychology.romm.base.AppDatabase;
import com.example.psychology.romm.dao.RadarChartDao;
import com.example.psychology.romm.entity.RadarChartData;
import com.example.psychology.ui.fragment.TrainingFragment2;
import com.example.psychology.ui.my.MyActivity;
import com.example.psychology.ui.view.IsPlayPopupWindow;
import com.king.wyk.fitnesstracker.Ble.BleCallback;
import com.king.wyk.fitnesstracker.Ble.BleHelper;
import com.king.wyk.fitnesstracker.Callback;
import com.king.wyk.fitnesstracker.FitnessTrackerModel;
import com.king.wyk.fitnesstracker.Log.Logger;
import com.king.wyk.fitnesstracker.ResponseData.BodyStateData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class HomeJavcActivity extends AppCompatActivity {

    private ActivityHomeBinding bind;

//    public static BleHelper bleHelper;
//    public static FitnessTrackerModel fitnessTracker;
    private String TAG = "TAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_home);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        StatusBar.INSTANCE.initStatusBarStyle(this, false, Color.parseColor("#ffffff"));
        initView();
        initData();

//        bleHelper = new BleHelper(this);
//        //获取手环
//        fitnessTracker = bleHelper.getFitnessTracker();
//
//        //添加ble状态回调
//        bleHelper.setBleCallback(new BleCallback() {
//            @Override
//            public void connectCallback() {
//                Log.e(TAG, "手环:手环连接成功 ");
//                new Thread(() -> {
//                    try {
//                        Thread.sleep(3000);
//                        fitnessTracker.clearDataCommand();
//                        Thread.sleep(3000);
//                        fitnessTracker.setTimeCommand((int) (System.currentTimeMillis() / 1000));
//                        Thread.sleep(3000);
//                        fitnessTracker.setMonitorCommand(true);
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }).start();
//
//
//                fitnessTracker.getBatteryLevel(new Callback<Integer>() {
//                    @Override
//                    public void onSuccess(Integer integer) {
//                        Log.e(TAG, "time: " + integer);
//                    }
//
//                    @Override
//                    public void onFailed(String s) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void disConnectCallback() {
//                Log.e(TAG, "手环:disConnectCallback ");
//                bleHelper.connect();
//            }
//
//            @Override
//            public void connectFailCallback(String s) {
//                Log.e(TAG, "手环:connectFailCallback ");
//                bleHelper.connect();
//            }
//        });
//        //连接
//        bleHelper.connect();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeleteEvent event) {
        if (event.getHead() != 0) {
            bind.ivHomeHead.setImageResource(event.getHead());
        }
        if (!StringUtils.isEmpty(event.getTitle())) {
            bind.tvHomeName.setText(event.getTitle());
        }
    }


    private List<HomeActivity.CatalogBean> list = new ArrayList<>();
    private HomeAdapter homeAdapter;
    private NavController controller;
    private IsPlayPopupWindow isPlayPopupWindow, getBattery;

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        init();
        RequestQX();


        list.add(new HomeActivity.CatalogBean("放松", R.mipmap.icon_relax, R.mipmap.icon_relax2, true));
        list.add(new HomeActivity.CatalogBean("冥想", R.mipmap.icon_meditation, R.mipmap.icon_meditation2, false));
        list.add(new HomeActivity.CatalogBean("报告", R.mipmap.icon_report, R.mipmap.icon_report2, false));
        list.add(new HomeActivity.CatalogBean("我的", R.mipmap.icon_wode, R.mipmap.icon_wode2, false));
        bind.rvHomeCatalog.setLayoutManager(linearLayoutManager);
        homeAdapter = new HomeAdapter(R.layout.layout_adapter_home, list);
        bind.rvHomeCatalog.setAdapter(homeAdapter);
        homeAdapter.setList(list);
        controller = Navigation.findNavController(this, R.id.flHome);

        homeAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (TrainingFragment2.isplay) {
                IsPlayPopupWindow isPlayPopupWindow = new IsPlayPopupWindow(HomeJavcActivity.this, bind.ivHomeHead, "视频正在播放,退出训练后您的数据\n将不会保留");
                isPlayPopupWindow.setOnClickListener(() -> {
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setBool(false);
                    }
                    list.get(position).setBool(true);
                    homeAdapter.setList(list);
                    switch (position) {
                        case 0:
                            controller.navigate(R.id.RelaxFragment);
                            break;
                        case 1:
                            controller.navigate(R.id.MeditationFragment);
                            break;
                        case 2:
                            controller.navigate(R.id.BreatheFragment);
                            break;
                        case 3:
                            controller.navigate(R.id.MyFragment);
                            break;
                    }
                });

            } else {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setBool(false);
                }
                list.get(position).setBool(true);
                homeAdapter.setList(list);
                switch (position) {
                    case 0:
                        controller.navigate(R.id.RelaxFragment);
                        break;
                    case 1:
                        controller.navigate(R.id.MeditationFragment);
                        break;
                    case 2:
                        controller.navigate(R.id.BreatheFragment);
                        break;
                    case 3:
                        controller.navigate(R.id.MyFragment);
                        break;
                }
            }
        });

        bind.ivHomeAfterSale.setOnClickListener(v -> {

            afterSale();
        });
        bind.ivHomeHead.setOnClickListener(v -> {
            if (TrainingFragment2.isplay) {
                isPlayPopupWindow = new IsPlayPopupWindow(this, bind.ivHomeHead, "视频正在播放,退出训练后您的数据\n将不会保留");
                isPlayPopupWindow.setOnClickListener(() -> {
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setBool(false);
                    }
                    list.get(3).setBool(true);
                    homeAdapter.setList(list);
                    controller.navigate(R.id.MyFragment);
                });
            } else {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setBool(false);
                }
                list.get(3).setBool(true);
                homeAdapter.setList(list);
                controller.navigate(R.id.MyFragment);
            }

        });
    }

    private PopupWindow afterSaleWindow;

    private void afterSale() {
        afterSaleWindow = new PopupWindow(this);
        View inflate = getLayoutInflater().inflate(R.layout.layout_popup_after_sale, null);
        afterSaleWindow.setContentView(inflate);
        afterSaleWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        afterSaleWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        afterSaleWindow.setBackgroundDrawable(new ColorDrawable(0));
        afterSaleWindow.setFocusable(true);
        afterSaleWindow.setOutsideTouchable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);

        afterSaleWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = getWindow().getAttributes();
            lp1.alpha = 1f;
            getWindow().setAttributes(lp1);
        });
        afterSaleWindow.showAtLocation(bind.ivHomeAfterSale, Gravity.CENTER, 0, 0);
        inflate.findViewById(R.id.iv_home_popup_x)
                .setOnClickListener(v -> afterSaleWindow.dismiss());
    }

    private void initData() {
        bind.ivHomeHead.setImageResource(LoginActivity.Companion.getHead());
        bind.tvHomeName.setText(LoginActivity.Companion.getName());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (afterSaleWindow != null) {
            afterSaleWindow.dismiss();
        }
        if (isPlayPopupWindow != null) {
            isPlayPopupWindow.dismiss();
            isPlayPopupWindow = null;
        }
    }

    class HomeAdapter extends BaseQuickAdapter<HomeActivity.CatalogBean, BaseViewHolder> {

        public HomeAdapter(int layoutResId, List<HomeActivity.CatalogBean> list) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder holder, HomeActivity.CatalogBean item) {
            holder.setText(R.id.tvHomeCatalog, item.getTitle())
                    .setTextColor(
                            R.id.tvHomeCatalog, item.isBool() ? getResources().getColor(R.color.white) : getResources().getColor(R.color.color_869397))
                    .setBackgroundResource(
                            R.id.llHomeCatalog, item.isBool() ? R.drawable.shape_home_catalog1 :
                                    R.drawable.shape_home_catalog2
                    )
                    .setImageResource(
                            R.id.ivHomeCatalog, item.isBool() ? item.getIcon2() : item.getIcon()
                    );
        }

    }


    private EgoXDevice egoXDevice;
    private BLEDevice bleDevice;

    private void init() {
        try {
            Log.e("TAG", "初始化蓝牙...");
            bleDevice = BLEDevice.getInstance(this);
            bleDevice.InitBle();
            egoXDevice = EgoXDevice.getInstance(this);
            egoXDevice.RegistHandle(handler);
            bleDevice.RegistHandle(handler, "activity");
            egoXDevice.SetAutoConn(true);
            handler.removeCallbacks(onCallDelayCallBack);

            JSONArray jsonArray = egoXDevice.GetBindList();
            Log.e("TAG", "绑定的设备列表:" + (jsonArray == null ? "-111" :
                    jsonArray.toString()));
            bleDevice.StartScan(5, 2, CusoftUtil.SCAN_MODE_BALANCED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            PublicPrintLog.onPrintlnSystemLn("计时222");
            handler.removeCallbacks(runnable);
            handler.removeCallbacks(onCallDelayCallBack);
            handler.postDelayed(onCallDelayCallBack, 2000);
        }
    };
    private int sss = 0;
    private Runnable onCallDelayCallBack = new Runnable() {
        @Override
        public void run() {
            sss++;
            PublicPrintLog.onPrintlnSystemLn("计数器...$sss");
            if (sss >= 10) {
                handler.sendEmptyMessage(1000);
            }
        }
    };

    private String FILE_READ = "android.permission.READ_EXTERNAL_STORAGE",
            FILE_WRITE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final int REQUEST_CODE_PERMISSION_CONTACTS = 10;

    /**
     * Android  6.0以上动态获取 模糊位置权限
     */
    private void RequestQX() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            init();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (
                    this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                            this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                            this.checkSelfPermission(FILE_READ) != PackageManager.PERMISSION_GRANTED ||
                            this.checkSelfPermission(FILE_WRITE) != PackageManager.PERMISSION_GRANTED ||
                            this.checkSelfPermission("android.permission.SET_TIME") != PackageManager.PERMISSION_GRANTED
            ) {
                //请求权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, FILE_WRITE, FILE_READ, Manifest.permission.SET_TIME},
                            REQUEST_CODE_PERMISSION_CONTACTS);
                }
                //判断是否需要 向用户解释，为什么要申请该权限
                if (this.shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                    Toast.makeText(this, "shouldShowRequestPermissionRationale",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
            }
        }
    }

    public static boolean _bIsConnectDevice = false;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("MissingPermission")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CusoftModel cusoftModel = msg.obj instanceof CusoftModel ? (CusoftModel) msg.obj : null;
            switch (msg.what) {
                case CusoftConfig.CUSOFT_MSG_BLE_CODE: // 错误消息
                    Log.e("TAG", "错误消息日志接收..." + msg.obj);
                    if (cusoftModel.getnErrorCode() == CusoftConfig.STATE_SCAN_BLE_FAIL) { //
                        // 扫描失败，需要重置蓝牙
                        bleDevice.ResetBLE();
                    }
                    break;
                case EgoXConfig.CUSOFT_MSG_EGOX_LIST: // 扫描结果
                    Log.e("TAG", "扫描到的设备信息:" + cusoftModel.getDevice().getName() + "  地址:" + cusoftModel.getDevice().getAddress());
                    if ("EGO_X BLE".equals(cusoftModel.getDevice().getName())) {
                        egoXDevice.Connect(cusoftModel.getStrDeviceAddress());

                    }
                    break;
                case CusoftConfig.CUSOFT_MSG_BLE_CONN_STATUS: // 连接状态通知
                    Log.e("TAG", "连接状态通知:" + msg.obj);
                    _bIsConnectDevice = cusoftModel.getConnCode() == CusoftConfig.STATE_CONNECTED;
                    Log.e("TAG", "连接状态通知:连接成功 = " + _bIsConnectDevice);
                    egoXDevice.BindDevice(cusoftModel.getStrDeviceName(),
                            cusoftModel.getStrDeviceAddress());
                    if (TrainingFragment2.isplay && !_bIsConnectDevice) {
                        if (TrainingFragment2.drillType == 2) {
                            EventBus.getDefault().post(new PlayEvent(false));
                            if (isPlayPopupWindow == null) {
                                isPlayPopupWindow = new IsPlayPopupWindow(HomeJavcActivity.this, bind.ivHomeHead, "请佩戴好您的脑波仪后继续训练");
                            } else {
                                if (!isPlayPopupWindow.isShowing()) {
                                    isPlayPopupWindow.showAtLocation(bind.ivHomeHead, Gravity.CENTER, 0, 0);
                                }
                            }
                        } else {
                            EventBus.getDefault().post(new PlayEvent(true));
                            if (isPlayPopupWindow != null) {
                                isPlayPopupWindow.dismiss();
                            }
                        }
                    } else {

                    }
                    break;
                case EgoXConfig.CUSOFT_MSG_EGOX_EEG://接收到的EEG消息
                    EgoxModel egoxModel = (EgoxModel) msg.obj;

                    int attention = egoxModel.getEegData().getAttention();//专注
                    int meditation = egoxModel.getEegData().getMeditation();//放松
                    number--;
                    if (TrainingFragment2.isplay && _bIsConnectDevice && number <= 0) {
                        number = 5;
                        if (attention == 0 && TrainingFragment2.drillType == 2) {
                            EventBus.getDefault().post(new PlayEvent(false));
                            if (isPlayPopupWindow == null) {
                                isPlayPopupWindow = new IsPlayPopupWindow(HomeJavcActivity.this, bind.ivHomeHead, "请佩戴好您的脑波仪后继续训练");
                            } else {
                                if (!isPlayPopupWindow.isShowing()) {
                                    isPlayPopupWindow.showAtLocation(bind.ivHomeHead, Gravity.CENTER, 0, 0);
                                }
                            }
                        } else {
                            EventBus.getDefault().post(new PlayEvent(true));
                            if (isPlayPopupWindow != null) {
                                isPlayPopupWindow.dismiss();
                            }
                        }
                    }

                    Log.e("TAG", "脑电：专注：" + attention + "  放松：" + meditation);
                    int delta = egoxModel.getEegData().getDelta();
                    int theta = egoxModel.getEegData().getTheta();
                    int lowAlpha = egoxModel.getEegData().getLowAlpha();
                    int highAlpha = egoxModel.getEegData().getHighAlpha();
                    int lowBeta = egoxModel.getEegData().getLowBeta();
                    int highBeta = egoxModel.getEegData().getHighBeta();
                    int lowGamma = egoxModel.getEegData().getLowGamma();
                    int midGamma = egoxModel.getEegData().getMidGamma();

                    if (attention > 90 || attention < 20) {
                        attention = 60;
                    }
                    if (meditation > 90 || meditation < 20) {
                        meditation = 60;
                    }
                    EventBus.getDefault().post(new MessageEvent(attention, meditation,
                            delta > 100000 ? 98000 : delta,
                            theta > 100000 ? 98000 : theta,
                            lowAlpha > 100000 ? 98000 : lowAlpha,
                            highAlpha > 100000 ? 98000 : highAlpha,
                            lowBeta > 100000 ? 98000 : lowBeta,
                            highBeta > 100000 ? 98000 : highBeta,
                            lowGamma > 100000 ? 98000 : lowGamma,
                            midGamma > 100000 ? 98000 : midGamma
                    ));
                    break;
                case 1000:
                    handler.removeCallbacks(onCallDelayCallBack);
                    break;
                case CusoftConfig.STATE_SCAN_BLE_FAIL:
                    PublicPrintLog.onPrintlnSystemLn("扫描失败回调");
                    bleDevice.ResetBLE();
                    break;
            }
        }
    };

    int number = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (isGpsEnable(this)) {
                        init();
                    } else {
                        Log.e("TAG", "没有GPS权限");
                    }

                } else {
                    //用户拒绝权限
                }
            }
        }
    }

    /**
     * Gps是否可用
     */
    private boolean isGpsEnable(final Context context) {
        LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }
}
