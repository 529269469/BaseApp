package com.example.psychology.base_app.utils;

import android.media.MediaPlayer;

public class LoopingPlayer {

    private MediaPlayer mediaPlayer;

    public LoopingPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public void goDo() {

        //  mediaPlayer.seekTo(0);//在当前位置播放
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int pos = mediaPlayer.getCurrentPosition();//当前播放时长
                    int dur = mediaPlayer.getDuration();//总时长
                    if (dur - pos <= 2000) {
                        // endPlay.playEnd(pos,dur);
                        //mediaPlayer.setLooping(true);
                        mediaPlayer.seekTo(0);
                        mediaPlayer.start();
                    }

                }
            }
        }.start();
    }
}