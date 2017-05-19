package com.projectcourse2.group11.smallbusinessmanager.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public abstract class Person implements Serializable{
    private String SSN; // yyyymmdd-xxxx
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email; //contains @
    private Address address;
    private Position position;
    private String UID;

    //TODO Test package-private.
    public Person(){}
    public Person(String SSN, String firstName, String lastName, String phoneNumber, String email, String UID) {
        this.SSN = SSN;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        if (email.contains("@")) {
            this.email = email;
        }
        this.UID=UID;
    }
    public void setPosition(Position position){
        this.position=position;
    }

    public Position getPosition(){return position;}

    public String getSSN() {
        return SSN;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email.contains("@")) {
            this.email = email;
        }
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Map<String,Object> toHashMap(){
        Map<String, Object> map = new HashMap<>();
        map.put(UID+"/SSN/",SSN);
        map.put(UID+"/firstName/",firstName);
        map.put(UID+"/lastName/",lastName);
        map.put(UID+"/phoneNumber/",phoneNumber);
        map.put(UID+"/email/",email);
        map.put(UID+"/position/",position);
        if (address!=null) {
            map.put("/workerAddress/" + UID + "/city/", address.getCity());
            map.put("/workerAddress/" + UID + "/country/", address.getCountry());
            map.put("/workerAddress/" + UID + "/postalCode/", address.getPostalCode());
            map.put("/workerAddress/" + UID + "/street/", address.getStreetNumber());
        }
        return map;
    }

}
