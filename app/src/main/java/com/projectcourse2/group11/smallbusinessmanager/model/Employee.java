package com.projectcourse2.group11.smallbusinessmanager.model;

public class Employee extends Person {
    private int contractID;
    private String title;
    private String employmentData;
    private String userName;
    private String password;
    private double hourCost;

    public Employee(String SSN, String firstName, String lastName, String phoneNumber, String email, String UID) {
        super(SSN, firstName, lastName, phoneNumber, email, UID);
    }

    public String printEmploymentDate(){
        return employmentData;
    }

    public int printContractID(){
        return contractID;
    }

    public String printTitle(){
        return title;
    }

    public double calculateSalary(){
        return (hourCost*8)*30;
    }

    public void viewAllInfo(){
        //to do
    }

    public void printContract(){
        //to do
    }

}
