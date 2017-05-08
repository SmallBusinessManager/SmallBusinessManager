package com.projectcourse2.group11.smallbusinessmanager.model;

public class Accountant extends Person {
    private Employee[] employees;
    private double availableMoney;

    public Accountant(String SSN, String firstName, String lastName, String phoneNumber, String email, String UID ,Employee[] employees, double availableMoney) {
        super(SSN, firstName, lastName, phoneNumber, email, UID);
        setPosition(Position.ACCOUNTANT);
        this.employees=employees;
        this.availableMoney=availableMoney;
    }

    public void listWorkers(){
        //to do
    }
    public void viewExpenses(){
        //to do
    }
    public void viewIncome(){
        //to do
    }
    public void approvePayment(){
        //to do
    }
    public void reviewContract(){
        //to do
    }
    public void viewProject(){
        //to do
    }
}
