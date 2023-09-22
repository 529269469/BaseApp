package com.example.psychology.base_app.utils;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class VideoPlayerUtils {
    private static VideoPlayerUtils instance;
    private StandardGSYVideoPlayer player;

    private VideoPlayerUtils() {
    }

    public static VideoPlayerUtils getInstance() {
        if (instance == null) {
            instance = new VideoPlayerUtils();
        }
        GSYVideoType.enableMediaCodec();
        return instance;
    }

    public void setVideo(Context context, StandardGSYVideoPlayer StandardGSYVideoPlayer, String imageUrl) {
        this.player = StandardGSYVideoPlayer;
        if (player != null) {
            player.release();
        }
        player.setUp(imageUrl, true, null);
        player.getBackButton().setVisibility(View.GONE);
        player.setRotateViewAuto(true);
        player.setShowFullAnimation(true);
//        player.setLockLand(false);
        player.setAutoFullWithSize(true);
        player.setLooping(true);

        player.setNeedShowWifiTip(false);
        //设置全屏按键功能
//        player.getFullscreenButton().setOnClickListener(v -> {
//            player.startWindowFullscreen(context, false, true);
//        });

        //增加封面
        ImageView imageView = new ImageView(context);

        Glide.with(context.getApplicationContext())
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1000000)
                                .centerInside()
                )
                .load(imageUrl)
                .into(imageView);

        player.setThumbImageView(imageView);
        player.startPlayLogic();



    }

    public void setVideo2(Context context, StandardGSYVideoPlayer StandardGSYVideoPlayer, String imageUrl) {
        this.player = StandardGSYVideoPlayer;
        if (player != null) {
            player.release();
        }
        player.setUp(imageUrl, true, null);
        player.getBackButton().setVisibility(View.GONE);
        player.setRotateViewAuto(false);
        player.setShowFullAnimation(true);
//        player.setLockLand(false);
        player.setAutoFullWithSize(true);
        player.setLooping(false);
        player.setNeedShowWifiTip(false);
        player.setHideKey(false);
        player.getSeekRatio();



        //设置全屏按键功能
        player.getFullscreenButton().setOnClickListener(v -> {
            player.setNeedOrientationUtils(false);
            player.startWindowFullscreen(context, true, true);
        });
        player.startPlayLogic();
        //增加封面
        ImageView imageView = new ImageView(context);

        Glide.with(context.getApplicationContext())
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1000000)
                                .centerInside()
                )
                .load(imageUrl)
                .into(imageView);

        player.setThumbImageView(imageView);
        player.startPlayLogic();

    }

    public void setClose() {
        if (player != null) {
            player.release();

        }

    }

}
