package com.example.psychology.base_app.utils.loadingWindow;

import android.content.Context;


/**
 * 正在加载对话框工具类
 */
public class LoadingUtil {
    public CustomProgressDialog progressDialog;

    /**
     * 方法说明 对话框显示
     */
    public void showProgressDialog(Context context) {
        try {
            if (null == progressDialog) {
                progressDialog = CustomProgressDialog.createDialog(context);
            }
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgressDialog(Context context, String messager) {
        try {
            if (null == progressDialog) {
                progressDialog = CustomProgressDialog.createDialog(context);
            }
            if (!progressDialog.isShowing()) {
                progressDialog.setMessage(messager);
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 方法说明 对话框关闭
     */
    public void dismissProgressDialog() {
        try {
            if (null != progressDialog && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}