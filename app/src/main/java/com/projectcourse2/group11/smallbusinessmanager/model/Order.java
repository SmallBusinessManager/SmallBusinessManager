package com.projectcourse2.group11.smallbusinessmanager.model;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private int id;
    private String description;
    private Date startDate;
    private Enum status;
    private java.util.Date finishDate;
    //    private Date finishDate;
    private Worker worker;
    private Project project;
    public Order(String description, Worker worker, Project project){
        this.id = 1;
        this.worker=worker;
        this.status=Status.NOT_STARTED;
        this.description=description;
        this.project=project;
        this.finishDate=null;
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
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        finishDate= new java.util.Date(ts.getTime());
    }

    public void assignWorker(Worker worker){
        this.worker=worker;
    }

    public Map<String,Object> toHashMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("/order/project_"+project.getId()+"/"+id+"/worker/",worker.getSSN());
        map.put("/order/project_"+project.getId()+"/"+id+"/status/",status.name());
        map.put("/order/project_"+project.getId()+"/"+id+"/description/",description);
        if (startDate!=null) {
            map.put("/order/project_" + project.getId() + "/" + id + "/startDate/", startDate.toString());
        }
        if (finishDate!=null) {
            map.put("/order/project_" + project.getId() + "/" + id + "/finishDate/", finishDate.toString());
        }
        return map;
    }
}
