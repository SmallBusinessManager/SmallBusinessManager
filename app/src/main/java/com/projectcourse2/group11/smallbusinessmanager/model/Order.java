package com.projectcourse2.group11.smallbusinessmanager.model;

public class Order {
    private int id;
    private String description;
    private Date startDate;
    private Enum status;
    private Date finishDate;
    private Worker worker;
    public Order(String description, Worker worker){
        this.id = 1;
        this.worker=worker;
        this.status=Status.NOT_STARTED;
        this.description=description;
    }

    public void edit(){
        //to do
    }

    public void delete(){
        //to do
    }

    public void print(){
        //to do
    }
    public void startOrder(Date startDate){
        this.startDate=startDate;
        status=Status.STARTED;
    }

    public void markAsFinished(){
        status=Status.FINISHED;
    }

    public void assignWorker(Worker worker){
        this.worker=worker;
    }
}
