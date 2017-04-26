package com.projectcourse2.group11.smallbusinessmanager.model;

public class Worker extends Person {
    private TeamLeader teamLeader;

    public Worker(String SSN, String firstName, String lastName, String phoneNumber, String email, Address address) {
        super(SSN, firstName, lastName, phoneNumber, email, address);
    }

    public void viewOrders(){
        //to do
    }

    public void viewWorkingHours(){
        //to do
    }
}
