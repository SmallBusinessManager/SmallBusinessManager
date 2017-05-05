package com.projectcourse2.group11.smallbusinessmanager.model;

public class TeamLeader extends Person {
    private Worker[] workers;
    private Order[] orders;
    private Manager manager;

    public TeamLeader(String SSN, String firstName, String lastName, String phoneNumber, String email, Address address) {
        super(SSN, firstName, lastName, phoneNumber, email/*, address*/);
        setPosition(Position.TEAM_LEADER);
    }

    public void viewAssignedWorkers(){
        //to do
    }

    public void viewAssignedOrders(){
        //to do
    }

    public void placeOrder(){
        //to do
    }

    public void addExpense(){
        //to do
    }

    public Manager getManager(){
    return this.manager;
    }
}
