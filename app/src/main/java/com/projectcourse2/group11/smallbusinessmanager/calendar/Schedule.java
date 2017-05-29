package com.projectcourse2.group11.smallbusinessmanager.calendar;

import java.io.Serializable;

public class Schedule implements Serializable {
    private String startTime;
    private String endTime;

    public Schedule() {
    }

    public Schedule(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
