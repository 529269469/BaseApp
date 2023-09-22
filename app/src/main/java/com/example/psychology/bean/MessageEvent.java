package com.example.psychology.bean;

public class MessageEvent {

    private int attention;//专注
    private int meditation;//放松
    private int delta;
    private int theta;
    private int lowAlpha;
    private int highAlpha;
    private int lowBeta;
    private int highBeta;
    private int lowGamma;
    private int midGamma;

    public MessageEvent(int attention, int meditation, int delta, int theta, int lowAlpha, int highAlpha, int lowBeta, int highBeta, int lowGamma, int midGamma) {
        this.attention = attention;
        this.meditation = meditation;
        this.delta = delta;
        this.theta = theta;
        this.lowAlpha = lowAlpha;
        this.highAlpha = highAlpha;
        this.lowBeta = lowBeta;
        this.highBeta = highBeta;
        this.lowGamma = lowGamma;
        this.midGamma = midGamma;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getMeditation() {
        return meditation;
    }

    public void setMeditation(int meditation) {
        this.meditation = meditation;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public int getTheta() {
        return theta;
    }

    public void setTheta(int theta) {
        this.theta = theta;
    }

    public int getLowAlpha() {
        return lowAlpha;
    }

    public void setLowAlpha(int lowAlpha) {
        this.lowAlpha = lowAlpha;
    }

    public int getHighAlpha() {
        return highAlpha;
    }

    public void setHighAlpha(int highAlpha) {
        this.highAlpha = highAlpha;
    }

    public int getLowBeta() {
        return lowBeta;
    }

    public void setLowBeta(int lowBeta) {
        this.lowBeta = lowBeta;
    }

    public int getHighBeta() {
        return highBeta;
    }

    public void setHighBeta(int highBeta) {
        this.highBeta = highBeta;
    }

    public int getLowGamma() {
        return lowGamma;
    }

    public void setLowGamma(int lowGamma) {
        this.lowGamma = lowGamma;
    }

    public int getMidGamma() {
        return midGamma;
    }

    public void setMidGamma(int midGamma) {
        this.midGamma = midGamma;
    }
}
