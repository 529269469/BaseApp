package com.example.psychology.bean;

public class HrvEvent {
    private int heartRate;//心率
    private int hrv;
    private int bloodOxygen;//血氧
    private float stress;//压力
    private float emotion;//情绪
    private float fatigue;//疲劳
    private Long time;


    public HrvEvent(int heartRate, int hrv, int bloodOxygen, float stress, float emotion, float fatigue, Long time) {
        this.heartRate = heartRate;
        this.hrv = hrv;
        this.bloodOxygen = bloodOxygen;
        this.stress = stress;
        this.emotion = emotion;
        this.fatigue = fatigue;
        this.time = time;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getHrv() {
        return hrv;
    }

    public void setHrv(int hrv) {
        this.hrv = hrv;
    }

    public int getBloodOxygen() {
        return bloodOxygen;
    }

    public void setBloodOxygen(int bloodOxygen) {
        this.bloodOxygen = bloodOxygen;
    }

    public float getStress() {
        return stress;
    }

    public void setStress(float stress) {
        this.stress = stress;
    }

    public float getEmotion() {
        return emotion;
    }

    public void setEmotion(float emotion) {
        this.emotion = emotion;
    }

    public float getFatigue() {
        return fatigue;
    }

    public void setFatigue(float fatigue) {
        this.fatigue = fatigue;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
