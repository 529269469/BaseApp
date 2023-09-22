package com.example.psychology.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.psychology.R;
import com.example.psychology.bean.PlayEvent;

import org.greenrobot.eventbus.EventBus;

public class IsPlayPopupWindow extends PopupWindow {

    public IsPlayPopupWindow(Activity context,View view,String text) {
        super(context);
        View inflate = context.getLayoutInflater().inflate(R.layout.layout_popup_free, null);
        setContentView(inflate);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(0));
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

        TextView viewById = inflate.findViewById(R.id.tv_free_content);
        viewById.setText(text);

        showAtLocation(view, Gravity.CENTER, 0, 0);
        inflate.findViewById(R.id.iv_free_popup_x)
                .setOnClickListener(v -> {
                    dismiss();
                });
        inflate.findViewById(R.id.bt_free_yes).setOnClickListener(v -> {
            dismiss();
            if (onClickListener!=null){
                onClickListener.setYes();
            }
        });
        if ("请佩戴好您的脑波仪后继续训练".equals(text)){
            inflate.findViewById(R.id.bt_free_no).setVisibility(View.VISIBLE);
        }else {
            inflate.findViewById(R.id.bt_free_no).setVisibility(View.GONE);
        }

        inflate.findViewById(R.id.bt_free_no).setOnClickListener(v -> {
            dismiss();
            EventBus.getDefault().post(new PlayEvent(false, 1));
        });


    }

    private onClickListener onClickListener;

    public void setOnClickListener(IsPlayPopupWindow.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface onClickListener{
        void setYes();

    }

    private onClickListenerNo onClickListenerNo;

    public void setOnClickListenerNo(onClickListenerNo onClickListener) {
        this.onClickListenerNo = onClickListenerNo;
    }
    public interface onClickListenerNo{
        void setNo();
    }


}
