package com.projectcourse2.group11.smallbusinessmanager.model;

public class Address {
    private String streetNumber;
    private String city;
    private String postalCode;
    private String Country;

    public Address(String streetNumber, String city, String postalCode, String country) {
        this.streetNumber = streetNumber;
        this.city = city;
        this.postalCode = postalCode;
        Country = country;
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
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public void validateInput(){
        //Country, City: editable drop menu for user to choose from
    }

    public void printAddress(){
        //to do
    }
}
