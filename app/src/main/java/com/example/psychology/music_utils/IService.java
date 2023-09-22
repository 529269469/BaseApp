package com.example.psychology.music_utils;

/**
 * Created by 52926 on 2017/4/28.
 */
public interface IService {
    void start();

    void pause();

    void resume();

    void seet(int i);

    void top();

    void next();

    void tiaomu(int p);

    boolean isPlay();

    int getSeekTo();
}
