package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.Calendar;

public abstract class Person {
    private String SSN; // yyyymmdd-xxxx
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email; //contains @
    private Address address;

    public Person(String SSN, String firstName, String lastName, String phoneNumber, String email, Address address) {
        if (isSSNValid(SSN)) {
            this.SSN = SSN;
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        if (email.contains("@")) {
            this.email = email;
        }
        this.address = address;
    }

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

    private boolean isSSNValid(String SSN) {
        SSN = SSN.trim().replace("-", "");
        int year = Integer.parseInt(SSN.substring(0, 3));
        int month = Integer.parseInt(SSN.substring(4, 5));
        int day = Integer.parseInt(SSN.substring(6, 7));

        return  !(SSN.isEmpty() || (SSN.length() != 12) || year > Calendar.getInstance().get(Calendar.YEAR) ||
                (month < 1 || month > 12) || (day < 0 || day > 31)) ;
    }

    public void printContactInfo() {
        //to do
    }

}
