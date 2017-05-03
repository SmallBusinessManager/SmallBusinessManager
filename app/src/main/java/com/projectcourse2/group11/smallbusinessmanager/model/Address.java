package com.projectcourse2.group11.smallbusinessmanager.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Address {
    private String streetNumber;
    private String city;
    private String postalCode;
    private String country;


    public Address(String streetNumber, String city, String postalCode, String country) {
        this.streetNumber = streetNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.country=country;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void validateInput(){
        //Country, City: editable drop menu for user to choose from
    }

    public void printAddress(){
        //to do
    }
    public Map<String, Object> toHasMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("/address/"+streetNumber+"/city/",city);
        map.put("/address/"+streetNumber+"/code/",postalCode);
        map.put("/address/"+streetNumber+"/country/",country);
        return map;
    }
}
