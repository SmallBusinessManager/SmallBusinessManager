package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.*;
import java.util.Date;

public class Expenses {
    private double amount;
    private String details;
    private String description;
    private java.util.Date date;
    private boolean apporoved;

    public Expenses(double amount, String details, String description, Date date) {
        this.amount = amount;
        this.details = details;
        this.description = description;
        this.date = date;
        apporoved = false;
    }

    public void setApporoved() {
        apporoved = true;
    }

    public double getAmount() {
        return amount;
    }

    public String getDetails() {
        return details;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }
}
