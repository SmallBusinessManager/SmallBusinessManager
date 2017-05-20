package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.ArrayList;

public class TeamLeader extends Person {
    private ArrayList<Worker> workers = new ArrayList<>();
    private ArrayList<Order> orders = new ArrayList<>();
    private Manager manager;

    public TeamLeader(){}
    public TeamLeader(String ssn, String firstName, String lastName, String phoneNumber, String email, String UID) {
        super(ssn, firstName, lastName, phoneNumber, email, UID);
        setPosition(Position.TEAM_LEADER);
    }

    public ArrayList viewAssignedWorkers(){
        return workers;
    }

    public ArrayList viewAssignedOrders(){
        return orders;
    }

    public void placeOrder(Order order){
        orders.add(order);
    }

    public void addExpense(){
        //to do
    }

    public Manager getManager(){
    return this.manager;
    }
}
