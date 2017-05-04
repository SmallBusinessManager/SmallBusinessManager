package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.HashMap;
import java.util.Map;

public class Project {
    private int id;
    private Manager manager;
    private Date startDate;
    private Date dueDate;

    public Project(Worker manager,Date startDate,Date dueDate){
        this.manager=new Manager(manager.getSSN(),manager.getFirstName(),manager.getLastName(),manager.getPhoneNumber(),manager.getEmail(),manager.getAddress());
        this.dueDate=dueDate;
        this.startDate=startDate;
        //
        this.id=2;
    }

    public void printInfo(){
        //to do
    }

    public void showAllOrders(){
        //to do
    }

    public void addExpenses(){
        //to do
    }

    public int getId(){
        return this.id;
    }

    public Manager getManager(){
        return this.manager;
    }

    public void addPayment(){
        //to do
    }

    public void getPayment(){
        //to do
    }
    public Map<String,Object> toHashMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("/project/"+id+"/manager/",manager.getSSN());
        map.put("/project/"+id+"/startDate/",startDate.toString());
        map.put("project/"+id+"/dueDate/",dueDate.toString());
        return map;
    }
}
