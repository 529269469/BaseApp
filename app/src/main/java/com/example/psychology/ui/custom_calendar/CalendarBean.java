package com.example.psychology.ui.custom_calendar;

import java.util.Date;


public class CalendarBean {
    //0：当月，1：上月，2：下一个月
    private int dayType;

    //星期
    private String weekOfDay;

    //日期
    private String day;

    //实际日期
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDayType() {
        return dayType;
    }

    public void setDayType(int dayType) {
        this.dayType = dayType;
    }

    public String getWeekOfDay() {
        return weekOfDay == null ? "" : weekOfDay;
    }

    public void setWeekOfDay(String weekOfDay) {
        this.weekOfDay = weekOfDay == null ? "" : weekOfDay;
    }

    public String getDay() {
        return day == null ? "" : day;
    }

    public void setDay(String day) {
        this.day = day == null ? "" : day;
    }
}