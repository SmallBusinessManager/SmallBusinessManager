package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.*;

public class Expenses {
    private String id = String.valueOf(UUID.randomUUID());
    private double amount;
    private String details;
    private String description;
    private String date;
    private boolean approved;

    public Expenses(double amount, String details, String description, String date) {
        this.amount = amount;
        this.details = details;
        this.description = description;
        this.date = date;
        approved = false;
    }

    public void setApproved() {
        approved = true;
    }

    public String getId() {
        return id;
    }

    public boolean isApproved() {
        return approved;
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

    public String getDate() {
        return date;
    }

    public Map<String, Object> toHashMap(String key) {
        Map<String, Object> map = new HashMap<>();
        map.put(id + "/id/", getId());
        map.put(id + "/amount/", getAmount());
        map.put(id+ "/details/", getDetails());
        map.put(id + "/description/", getDescription());
        map.put(id + "/date/", getDate());
        map.put(id + "/approved/", isApproved());

        return map;
    }
}
