package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.*;
import java.util.Date;

/**
 * Created by Bjarni on 17/05/2017.
 */

public class Invoice {
    private String amount;
    private String customerId;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Invoice(String id, String amount, String customerId) {
        this.id=id;
        this.customerId = customerId;
        this.amount = amount;
// invoiceTime = new Date().getTime();
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
        map.put(id + "/customerid/", getCustomerId());
        map.put(id + "/amount/",getAmount());
        return map;
    }
}
