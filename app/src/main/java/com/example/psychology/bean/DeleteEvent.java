package com.example.psychology.bean;

public class DeleteEvent {
    private int seekto;
    private int head;
    private int number;
    private String title;

    private boolean isStop;

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSeekto() {
        return seekto;
    }

    public void setSeekto(int seekto) {
        this.seekto = seekto;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
