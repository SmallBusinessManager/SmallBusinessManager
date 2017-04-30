package com.projectcourse2.group11.smallbusinessmanager.model;

public class Manager extends Person {
    private TeamLeader teamLeader;
    private Project project;

    public Manager(String SSN, String firstName, String lastName, String phoneNumber, String email, Address address) {
        super(SSN, firstName, lastName, phoneNumber, email, address);
    }

    public void createWorkOrders(){
        //to to
    }

    public Project getProject(){
        return this.project;
    }

    public TeamLeader getTeamLeader(){
        return this.teamLeader;
    }

    public void addExpense(){
        //to to
    }

    public void approvePayment(){
        //to to
    }
}
