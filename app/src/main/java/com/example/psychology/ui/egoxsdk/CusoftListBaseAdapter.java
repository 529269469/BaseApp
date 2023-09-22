package com.example.psychology.ui.egoxsdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cusoft.android.bledevice.model.CusoftModel;
import com.cusoft.android.bledevice.util.PublicPrintLog;
import com.example.psychology.R;

import java.util.List;

public class CusoftListBaseAdapter extends BaseAdapter
{
    private List arrData ;
    private Activity activity ;
    public CusoftListBaseAdapter(Activity activity, List arrData)
    {
        this.activity = activity ;
        this.arrData = arrData ;
    }

    public void setArrData(List arrData) {
        this.arrData = arrData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arrData == null ? 0 : arrData.size() ;
    }

    @Override
    public Object getItem(int i) {
        return arrData == null || arrData.size() <= i ? null : arrData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("MissingPermission")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Item item = null ;
        if(view == null)
        {
            view = View.inflate(activity, R.layout.activity_cusoft_egox_item,null);
            item = new Item() ;
            item.name = view.findViewById(R.id.item_devicename) ;
            item.address = view.findViewById(R.id.item_deviceaddress) ;
        }
        else
            item = (Item) view.getTag();
        BluetoothDevice device = ((CusoftModel) getItem(i)).getDevice();
        if(device != null && item != null)
        {
            PublicPrintLog.onPrintlnLog("设备名称:"+device.getName()+"  ...  " + item);
            item.name.setText(TextUtils.isEmpty(device.getName()) ? "" : device.getName());
            item.address.setText(TextUtils.isEmpty(device.getAddress()) ? "" : device.getAddress());
        }
        return view;
    }
    class Item
    {
        private TextView name,address;

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public TextView getAddress() {
            return address;
        }

        public void setAddress(TextView address) {
            this.address = address;
        }
    }
}
