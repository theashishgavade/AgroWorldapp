package com.project.agroworldapp.taskmanager.model;

import java.io.Serializable;

public class TimeModel implements Serializable {

    private int hour;
    private int minute;
    private String timeToNotify;

    public TimeModel(int hour, int minute, String timeToNotify) {
        this.hour = hour;
        this.minute = minute;
        this.timeToNotify = timeToNotify;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getTimeToNotify() {
        return timeToNotify;
    }

    public void setTimeToNotify(String timeToNotify) {
        this.timeToNotify = timeToNotify;
    }
}
