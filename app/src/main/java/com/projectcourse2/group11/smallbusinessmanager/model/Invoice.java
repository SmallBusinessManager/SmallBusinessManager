package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.*;
import java.util.Date;

/**
 * Created by Bjarni on 17/05/2017.
 */

public class Invoice {
    private double amount;
    private String customerId;
    private long invoiceTime;

    public Invoice(double amount, String customerId) {
        this.amount = amount;
        this.customerId = customerId;
        invoiceTime = new Date().getTime();
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
