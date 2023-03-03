package com.project.agroworld.ui.taskManager;

import java.io.Serializable;

public class TimeModel implements Serializable {

    private int hour;
    private int minute;
    private int am_pm;

    public TimeModel(int hour, int minute, int am_pm){
        this.hour = hour;
        this.minute = minute;
        this.am_pm = am_pm;
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

    public int getAm_pm() {
        return am_pm;
    }

    public void setAm_pm(int am_pm) {
        this.am_pm = am_pm;
    }
}
