package com.example.psychology.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.media3.common.C;

import com.example.psychology.R;

public class LoadingDialog extends PopupWindow {
    private ImageView iv_ing;
    private AnimationSet animationSet;


    public LoadingDialog(@NonNull Activity context,View view) {
        super(context);
        View inflate = context.getLayoutInflater().inflate(R.layout.dialog_loading, null);
        setContentView(inflate);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
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
            iv_ing.clearAnimation();
        });

        iv_ing = inflate.findViewById(R.id.iv_ing);
        showAtLocation(view, Gravity.CENTER, 0, 0);
        //加载动画
        loadIng();

    }



    //加载动画
    private void loadIng() {
        animationSet = new AnimationSet(true);
        RotateAnimation animation_rotate = new RotateAnimation(0, +359,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animation_rotate.setRepeatCount(-1);
        animation_rotate.setStartOffset(0);
        animation_rotate.setDuration(1000);
        LinearInterpolator lir = new LinearInterpolator();
        animationSet.setInterpolator(lir);
        animationSet.addAnimation(animation_rotate);
        iv_ing.startAnimation(animationSet);//开始播放
    }


}

