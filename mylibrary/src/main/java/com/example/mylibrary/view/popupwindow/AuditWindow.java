package com.example.mylibrary.view.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.mylibrary.R;

public class AuditWindow extends PopupWindow {
    private View conentView;
    private TextView tv_text1;
    private TextView tv_text2;
    private TextView tv_text3;
    private TextView tv_content;
    private ImageView iv_close;

    public AuditWindow(Activity context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.audit_popu_window, null);
        initView(conentView);
        setContentView(conentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setOutsideTouchable(true);

        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 0.7f;
        context.getWindow().setAttributes(lp);

        setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = context.getWindow().getAttributes();
            lp1.alpha = 1f;
            context.getWindow().setAttributes(lp1);
        });

        iv_close.setOnClickListener(v -> dismiss());


    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else {
            this.dismiss();
        }
    }


    public void setConent(String text1,String text2,String text3,String content){
        tv_text1.setText(text1);
        tv_text2.setText(text2);
        tv_text3.setText(text3);
        tv_content.setText(content);
    }

    private void initView(View conentView) {
        tv_text1 = conentView.findViewById(R.id.tv_text1);
        tv_text2 = conentView.findViewById(R.id.tv_text2);
        tv_text3 = conentView.findViewById(R.id.tv_text3);
        tv_content = conentView.findViewById(R.id.tv_content);
        iv_close = conentView.findViewById(R.id.iv_close);
    }
}
