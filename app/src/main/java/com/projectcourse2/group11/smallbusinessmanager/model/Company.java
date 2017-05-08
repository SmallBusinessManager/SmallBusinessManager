package com.projectcourse2.group11.smallbusinessmanager.model;

/**
 * Created by Phil on 5/6/2017.
 */

public class Company {
    private String companyName;
    private String ownerUID;

    public Company(String companyName, String ownerUID){
        this.companyName = companyName;
        this.ownerUID = ownerUID;
    }

    public String getCompanyName(){
        return companyName;
    }
    public String getOwnerUID(){
        return ownerUID;
    }
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }
    public void setOwnerUID(String ownerUID){
        this.ownerUID = ownerUID;
    }
}
