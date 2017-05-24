package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.*;
import java.util.Date;

public class Invoice {
    private String amount;
    private String customerId;
    private String id;

    public Invoice() {
    }

    public Invoice(String id,String amount) {
        this.id = id;
        this.amount = amount;
        //this.customerId = customerId;
        // invoiceTime = new Date().getTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Map<String, Object> toHashMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(id + "/id/", getId());
        map.put(id + "/customerId/", getCustomerId());
        map.put(id + "/amount/", getAmount());
        return map;
    }
}
