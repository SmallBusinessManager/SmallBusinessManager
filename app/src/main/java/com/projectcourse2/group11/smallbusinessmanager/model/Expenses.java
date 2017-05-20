package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.*;
import java.util.Date;

public class Expenses {
    private UUID id;
    private double amount;
    private String details;
    private String description;
    private java.util.Date date;
    private boolean apporoved;

    public Expenses(double amount, String details, String description, Date date) {
        this.id = UUID.randomUUID();
        this.amount = amount;
        this.details = details;
        this.description = description;
        this.date = date;
        apporoved = false;
    }

    public void setApporoved() {
        apporoved = true;
    }

    public UUID getId() {
        return id;
    }

    public boolean isApporoved() {
        return apporoved;
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

    public Map<String, Object> toHashMap(String key) {
        Map<String, Object> map = new HashMap<>();
        map.put(id + "/id/", getId());
        map.put(id + "/amount/", getAmount());
        map.put(id+ "/details/", getDetails());
        map.put(id + "/description/", getDescription());
        map.put(id + "/date/", getDate());

        return map;
    }
}
