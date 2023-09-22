package com.example.psychology.music_utils;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by 52926 on 2017/4/28.
 */

public class MusicService extends Service {
    private MediaPlayer player;
    private String path;
    private String[] str;
    private int position;

    @Override
    public void onCreate() {
        super.onCreate();
        play();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                position++;
                if (position == str.length) {
                    position = 0;
                }
                music(position);
            }
        });


    }

    class MyBinder extends Binder implements IService {
        public void start() {
            music(0);

        }

        public void pause() {
            player.pause();
        }

        @Override
        public void resume() {
            player.start();
        }

        @Override
        public void seet(int i) {
            player.seekTo(i);

        }

        @Override
        public void top() {
            position--;
            if (position == -1) {
                position = str.length - 1;
            }
            music(position);
        }

        @Override
        public void next() {
            position++;
            if (position == str.length) {
                position = 0;
            }
            music(position);
        }

        @Override
        public void tiaomu(int p) {
            music(p);
        }

        @Override
        public boolean isPlay() {
            return player.isPlaying();
        }

        @Override
        public int getSeekTo() {
            return player.getCurrentPosition();
        }


    }

    private void play() {
        player = new MediaPlayer();
        player.reset();
        path = getExternalFilesDir(null).toString()+"/psychology/music";
        File file = new File(path);
        str = file.list();
        for (int i = 0; i < str.length; i++) {

            try {
                player.setDataSource(path + "/" + str[i]);
                Log.e("TAG", "play: "+path + "/" + str[i]);
                player.prepare();
                updatePregress();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    private void music(int position) {
        if (player.isPlaying()) {
            player.stop();
        }
        player.reset();
        if (timer != null) {
            timer.cancel();
        }
        try {
            player.setDataSource(path + "/" + str[position]);
            player.prepare();
            player.seekTo(0);
            player.start();
            updatePregress();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private Timer timer;

    private void updatePregress() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putInt("dur", player.getDuration());
                bundle.putInt("cur", player.getCurrentPosition());
                message.setData(bundle);
                MusicFragment.handler.sendMessage(message);
            }
        };
        /**
         * @param
         * 500 : 调用这个方法时,向后推迟500毫秒再执行
         * 1000: 以后每个1000毫秒就执行一次这个任务;
         */
        timer.schedule(timerTask, 500, 1000);

    }

}
