package com.projectcourse2.group11.smallbusinessmanager.model;

public class Employee extends Person {
    private int contractID;
    private String title;
    private String employmentData;
    private String userName;
    private String password;
    private double hourCost;
    private Project assignment;

    public Employee(String SSN, String firstName, String lastName, String phoneNumber, String email, Address address) {
        super(SSN, firstName, lastName, phoneNumber, email, address);
    }

    public void printEmploymentDate(){
        //to do
    }

    public void printContractID(){
        //to do
    }

    public void printTitle(){
        //to do
    }

    public void calculateSalary(){
        //to do
    }

    public void viewAllInfo(){
        //to do
    }

    public void printContract(){
        //to do
    }

}
