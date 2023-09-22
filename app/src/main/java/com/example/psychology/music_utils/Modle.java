package com.example.psychology.music_utils;

/**
 * Created by 52926 on 2017/4/28.
 */

public class Modle {
    String name;
    String time;
    String path;

    public Modle(String name, String time, String path) {
        this.name = name;
        this.time = time;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
