package com.example.psychology.bean;

public class PlayEvent {

    private boolean isStop;

    private int type;

    public PlayEvent(boolean isStop, int type) {
        this.isStop = isStop;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public PlayEvent(boolean isStop) {
        this.isStop = isStop;
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }
}
