package com.projectcourse2.group11.smallbusinessmanager.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 5/6/2017.
 */

public class Company implements Serializable {
    private String companyName;
    private String address;
    private String city;
    private String ownerUID;

    public Company(){}
    public Company(String companyName, String ownerUID){
        this.companyName = companyName;
        this.ownerUID = ownerUID;
        this.address = "";
        this.city = "";
    }

    public String getCompanyName(){
        return companyName;
    }
    public String getOwnerUID(){
        return ownerUID;
    }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }
    public void setOwnerUID(String ownerUID){
        this.ownerUID = ownerUID;
    }
    public void setAddress(String address) { this.address = address; }
    public void setCity(String city) { this.city = city; }
}
