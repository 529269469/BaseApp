package com.example.psychology.base_app.utils.loadingWindow;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.psychology.R;

/**
 * 自定义的加载进度对话框
 */
public class CustomProgressDialog extends Dialog {
    private static CustomProgressDialog customProgressDialog = null;
    private final Context context;

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public static CustomProgressDialog createDialog(Context context) {

        customProgressDialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.dialog_loading_view);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        customProgressDialog.setMessage("");// 默认提示语
        customProgressDialog.setCanceledOnTouchOutside(false);//是否允许点击弹窗外围取消弹窗
        return customProgressDialog;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (customProgressDialog == null) {
            return;
        }
        ImageView progressBar = customProgressDialog.findViewById(R.id.progressBar);
//        progressBar.setBackgroundResource(R.drawable.jiazaifong);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.dialog_progress_anim);
        progressBar.startAnimation(animation);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * setMessage 提示内容
     */
    public CustomProgressDialog setMessage(String strMessage) {
        TextView tvMsg = customProgressDialog.findViewById(R.id.tv_loadingmsg);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        return customProgressDialog;
    }
}