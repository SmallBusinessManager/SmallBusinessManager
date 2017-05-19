package com.projectcourse2.group11.smallbusinessmanager.model;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Order {
    private UUID id;
    private String description;
    private Date startDate;
    private Enum status;
    private Date finishDate;
    private Worker worker;
    private String projectID;

    public Order(){}

    public Order(String id,String description, Worker worker, String projectID){
        this.id=UUID.fromString(id);
        this.worker=worker;
        this.status=Status.NOT_STARTED;
        this.description=description;
        this.projectID=projectID;
        this.finishDate=null;
        this.status=Status.NOT_STARTED;
    }

    public Order(String description, Worker worker, String projectID){
        this.id=UUID.randomUUID();
        this.worker=worker;
        this.status=Status.NOT_STARTED;
        this.description=description;
        this.projectID=projectID;
        this.finishDate=null;
        this.status=Status.NOT_STARTED;
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
    public void setID(String id){
        this.id= UUID.fromString(id);
    }
    public String getProjectID(){
        return projectID;
    }

    public String getWorkerSSN() {
        return worker.getSsn();
    }

    public  String getDescription(){
        return description;
    }
    public void startOrder(){
        this.startDate= new Date(Calendar.getInstance().DAY_OF_MONTH,Calendar.getInstance().MONTH,Calendar.getInstance().YEAR);
        status=Status.STARTED;
    }

    public void markAsFinished(){
        this.finishDate= new Date(Calendar.getInstance().DAY_OF_MONTH,Calendar.getInstance().MONTH,Calendar.getInstance().YEAR);
        this.status=Status.FINISHED;
    }
    public String getId(){
        return String.valueOf(id);
    }

    public void assignWorker(Worker worker){
        this.worker=worker;
    }

    public Map<String,Object> toHashMap(){
        Map<String,Object> map = new HashMap<>();
        map.put(id+"/workerSSN/",worker.getSsn());
        map.put(id+"/status/",status);
        map.put(id+"/description/",description);
        map.put(id+"/projectID/",projectID);
        if (startDate!=null) {
            map.put(id + "/startDate/", startDate.toString());
        }
        if (finishDate!=null) {
            map.put(id  + "/finishDate/", finishDate.toString());
        }
        return map;
    }

}
