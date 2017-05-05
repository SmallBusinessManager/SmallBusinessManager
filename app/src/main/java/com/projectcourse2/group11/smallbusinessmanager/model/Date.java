package com.projectcourse2.group11.smallbusinessmanager.model;

public class Date {
    private int day;
    private int month;
    private int year;

    public Date(int day, int month, int year) {
        if (day >= 1 && day <= 31) {
            this.day = day;
        }
        if (month >= 1 && month <= 12) {
            this.month = month;
        }
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString(){
        return day+"/"+month+"/"+year;
    }
}
