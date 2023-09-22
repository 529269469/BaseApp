package com.example.psychology.ui.egoxsdk;

import static android.bluetooth.BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.blankj.utilcode.util.ConvertUtils;

import com.cusoft.android.bledevice.control.BLEDevice;
import com.cusoft.android.bledevice.model.CusoftModel;
import com.cusoft.android.bledevice.util.CusoftConfig;
import com.cusoft.android.bledevice.util.CusoftUtil;
import com.cusoft.android.bledevice.util.PublicPrintLog;
import com.cusoft.android.egox.EgoXDevice;
import com.cusoft.android.egox.model.EgoxModel;
import com.cusoft.android.egox.util.EgoXConfig;
import com.example.psychology.R;



import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.CRC32;


public class CusoftEgoxSdkActivity extends Activity implements View.OnClickListener,
        AdapterView.OnItemClickListener {
    private static final int REQUEST_CODE_PERMISSION_CONTACTS = 10;
    private static final int REQUEST_CODE_PERMISSION_GPS = 10;
    private String FILE_READ = "android.permission.READ_EXTERNAL_STORAGE",
            FILE_WRITE = "android.permission.WRITE_EXTERNAL_STORAGE";

    private EgoXDevice egoXDevice = null;
    private BLEDevice bleDevice = null;
    private ArrayList<CusoftModel> arrScanDevices = null;
    // 标记是否连接过设备
    private boolean _bIsConnectDevice = false;
    private int sss = 0;
    private Button btnStartScan, btnStopScan, btnSdkVer, btnBattery, btnDeviceModel, btnDeviceSn,
            btnDeviceVer, btnDeviceMac;
    private TextView pConnectState, txtSdkVer, txtBattery, txtDeviceModel, txtDeviceSn,
            txtDeviceVer, txtDeviceMac, txtShowSign, txtShowMac;
    private ListView listView;
    private CusoftListBaseAdapter adapter;
    // 记录最后连接过的设备
    private BluetoothDevice pLastConnDevice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cusoft_egox);
        PublicPrintLog.setISSTOP(false);
        arrScanDevices = new ArrayList<>();

        btnStartScan = findViewById(R.id.startScan);
        btnStopScan = findViewById(R.id.StopScan);
        pConnectState = findViewById(R.id.bleConnectState);
        listView = findViewById(R.id.bleDeviceLists);

        btnSdkVer = findViewById(R.id.btnGetSdkVersion);
        btnBattery = findViewById(R.id.btnGetBattery);
        btnDeviceModel = findViewById(R.id.btnGetDeviceModel);
        btnDeviceSn = findViewById(R.id.btnGetDeviceSn);
        btnDeviceVer = findViewById(R.id.btnGetDeviceVersion);
        btnDeviceMac = findViewById(R.id.btnGetBleMac);

        txtShowSign = findViewById(R.id.showSign);
        txtShowMac = findViewById(R.id.showMac);

        txtSdkVer = findViewById(R.id.SdkVersion);
        txtBattery = findViewById(R.id.showBattery);
        txtDeviceModel = findViewById(R.id.showDeviceModel);
        txtDeviceSn = findViewById(R.id.showDeviceSn);
        txtDeviceVer = findViewById(R.id.showDeviceVersion);
        txtDeviceMac = findViewById(R.id.showBleMac);

        btnSdkVer.setOnClickListener(this);
        btnBattery.setOnClickListener(this);
        btnDeviceModel.setOnClickListener(this);
        btnDeviceSn.setOnClickListener(this);
        btnDeviceVer.setOnClickListener(this);
        btnDeviceMac.setOnClickListener(this);


        adapter = new CusoftListBaseAdapter(this, null);
        listView.setAdapter(adapter);


        btnStartScan.setOnClickListener(this);
        btnStopScan.setOnClickListener(this);
        pConnectState.setOnClickListener(this);
        listView.setOnItemClickListener(this);

        String str = "";
        for (int i = 0; i < 6; i++) {
            str += "AC9A22A152F2".substring(i * 2, (i + 1) * 2) + (i == 5 ? "" : ":");
        }

        PublicPrintLog.onPrintlnLog("==================================== " + str);

//        init();
        RequestQX();


    }

    private void init() {
        try {
            PublicPrintLog.onPrintlnLog("初始化蓝牙...");
            bleDevice = BLEDevice.getInstance(this);
            bleDevice.InitBle();
            egoXDevice = EgoXDevice.getInstance(this);
            egoXDevice.RegistHandle(handler);
            bleDevice.RegistHandle(handler, "activity");
            egoXDevice.SetAutoConn(true);
            handler.removeCallbacks(onCallDelayCallBack);
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

    private Runnable onCallDelayCallBack = new Runnable() {
        @Override
        public void run() {
            sss++;
            PublicPrintLog.onPrintlnSystemLn("计数器..." + sss);

            if (sss >= 10) {
                handler.sendEmptyMessage(1000);
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("MissingPermission")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            PublicPrintLog.onPrintlnLog(msg.what + "接收到的handler消息..." + msg.obj);
            CusoftModel cusoftModel = msg.obj instanceof CusoftModel ? (CusoftModel) msg.obj : null;

            switch (msg.what) {
                case CusoftConfig.CUSOFT_MSG_BLE_CODE: // 错误消息
                    PublicPrintLog.onPrintlnLog("错误消息日志接收..." + msg.obj);

                    if (cusoftModel.getnErrorCode() == CusoftConfig.STATE_SCAN_BLE_FAIL) { //
                        // 扫描失败，需要重置蓝牙
                        bleDevice.ResetBLE();
                    }

                    break;
                case EgoXConfig.CUSOFT_MSG_EGOX_LIST: // 扫描结果

                    PublicPrintLog.onPrintlnLog("扫描到的设备信息:" + cusoftModel.getDevice().getName() + "  地址:" + cusoftModel.getDevice().getAddress());

                    addNewDevice(cusoftModel);
                    adapter.setArrData(arrScanDevices);

                    break;
                case CusoftConfig.CUSOFT_MSG_BLE_CONN_STATUS: // 连接状态通知
                    PublicPrintLog.onPrintlnLog("连接状态通知:" + msg.obj);
                    _bIsConnectDevice = cusoftModel.getConnCode() == CusoftConfig.STATE_CONNECTED;
                    PublicPrintLog.onPrintlnLog("连接状态通知:连接成功 = " + _bIsConnectDevice);
                    pConnectState.setText(_bIsConnectDevice ? "连接成功" : "连接断开");
                    pLastConnDevice = cusoftModel.getDevice();
                    egoXDevice.BindDevice(cusoftModel.getStrDeviceName(),
                            cusoftModel.getStrDeviceAddress());

                    txtShowMac.setText("设备地址:" + pLastConnDevice.getAddress());
                    break;
                case EgoXConfig.CUSOFT_MSG_EGOX_EEG://接收到的EEG消息
                    EgoxModel egoxModel = (EgoxModel) msg.obj;
                    Log.e("TAG", "脑电: " +
                            "   delta:" + egoxModel.getEegData().getDelta()
                            + "  lowAlpha:" + egoxModel.getEegData().getLowAlpha()
                            + "  highAlpha:" + egoxModel.getEegData().getHighAlpha()
                            + "  highBeta:" + egoxModel.getEegData().getHighBeta());


                    PublicPrintLog.onPrintlnLog("接收到的消息通知..:" + egoxModel.getEegData().getSignal());
                    txtShowSign.setText("信号值:" + egoxModel.getEegData().getSignal() + "    专注度:" + egoxModel.getEegData().getAttention() + "    放松度:" + egoxModel.getEegData().getMeditation());
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

    /**
     * Android  6.0以上动态获取 模糊位置权限
     */
    private void RequestQX() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            pBleControl.Connect(null);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户已授权
//                    pBleControl.Connect(null);
                    if (isGpsEnable(this)) {
                        init();
                    } else {
                        PublicPrintLog.onPrintlnLog("没有GPS权限");
                    }

                } else {
                    //用户拒绝权限
                }
                return;
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

    private String TAG = "TAG";



    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startScan:
                JSONArray jsonArray = egoXDevice.GetBindList();
                PublicPrintLog.onPrintlnLog("绑定的设备列表:" + (jsonArray == null ? "-111" :
                        jsonArray.toString()));
                bleDevice.StartScan(5, 2, CusoftUtil.SCAN_MODE_BALANCED);

                break;
            case R.id.StopScan:
                bleDevice.StopScan();

                break;
            case R.id.bleConnectState:
                if (pLastConnDevice != null) {
                    try {
                        if (bleDevice.IsConnected(pLastConnDevice.getName(),
                                pLastConnDevice.getAddress()))
                            egoXDevice.DisConnect(pLastConnDevice.getAddress());
                        else {
                            egoXDevice.Connect(pLastConnDevice.getAddress());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                break;
            case R.id.btnGetSdkVersion:
//                txtSdkVer.setText(CusoftUtil.getFormatString(bleDevice.GetVersion()));
                if (!btnSdkVer.getText().equals("解绑设备")) {
                    btnSdkVer.setText("解绑设备");
                    egoXDevice.BindDevice(pLastConnDevice.getName(), pLastConnDevice.getAddress());
                } else {
                    egoXDevice.GetBindList();
                    btnSdkVer.setText("绑定设备");
                    egoXDevice.UnbindDevice(pLastConnDevice.getName(),
                            pLastConnDevice.getAddress());
                }
                break;
            case R.id.btnGetBattery:
                txtBattery.setText(CusoftUtil.getFormatString(egoXDevice.GetBattery(pLastConnDevice.getAddress()) + ""));
                break;
            case R.id.btnGetDeviceModel:
                txtDeviceModel.setText(CusoftUtil.getFormatString(egoXDevice.GetDeviceModel(pLastConnDevice.getAddress())));
                break;
            case R.id.btnGetDeviceVersion:
                txtDeviceVer.setText(CusoftUtil.getFormatString(egoXDevice.GetDeviceVersion(pLastConnDevice.getAddress())));
                break;
            case R.id.btnGetDeviceSn:
                txtDeviceSn.setText(CusoftUtil.getFormatString(egoXDevice.GetDeviceSN(pLastConnDevice.getAddress())));
                break;
            case R.id.btnGetBleMac:
                txtDeviceMac.setText(CusoftUtil.getFormatString(egoXDevice.GetDeviceMAC(pLastConnDevice.getAddress())));
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PublicPrintLog.onPrintlnLog("执行了 onPause ");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        BluetoothDevice device = ((CusoftModel) adapterView.getItemAtPosition(i)).getDevice();

        if (egoXDevice != null) {
            egoXDevice.Connect(device.getAddress());
        }
    }

    @SuppressLint("MissingPermission")
    private void addNewDevice(CusoftModel cusoftModel) {

        if (cusoftModel == null || cusoftModel.getDevice() == null || TextUtils.isEmpty(cusoftModel.getDevice().getName()))
            return;

        if (arrScanDevices == null || arrScanDevices.size() <= 0) {
            arrScanDevices = new ArrayList<>();
            arrScanDevices.add(cusoftModel);
            return;
        }

        for (int i = 0; i < arrScanDevices.size(); i++) {
            if (arrScanDevices.get(i).getDevice().getAddress().equals(cusoftModel.getDevice().getAddress())) {
                PublicPrintLog.onPrintlnSystemLn("列表中的mac:" + arrScanDevices.get(i).getDevice().getAddress() + "  扫描到的：" + cusoftModel.getDevice().getAddress());
                return;
            }
        }

        arrScanDevices.add(cusoftModel);

    }


    private byte[] hexStr2bytes(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toUpperCase().toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (hexChar2byte(achar[pos]) << 4 | hexChar2byte(achar[pos + 1]));
        }
        return result;
    }

    private static int hexChar2byte(char c) {
        switch (c) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'a':
            case 'A':
                return 10;
            case 'b':
            case 'B':
                return 11;
            case 'c':
            case 'C':
                return 12;
            case 'd':
            case 'D':
                return 13;
            case 'e':
            case 'E':
                return 14;
            case 'f':
            case 'F':
                return 15;
            default:
                return -1;
        }
    }
}