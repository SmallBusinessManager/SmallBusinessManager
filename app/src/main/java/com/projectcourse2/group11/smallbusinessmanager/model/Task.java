package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.HashMap;
import java.util.Map;

public class Task {
    private enum Priority {HIGH, MEDIUM, LOW, NO_PRIORITY}

    private String id;
    private String name;
    private String description;
    //private String projectID;
    //private String PersonID;
    private Date startDate;
    private Date endDate;

    public Task(String name, String description, Date startDate, Date endDate) {
        this.name = name;
        this.description = description;
        //this.projectID = project;
        this.startDate = startDate;
        this.endDate = endDate;
        //this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*public String getProject() {
        return projectID;
    }

    public void setProject(String project) {
        this.projectID = project;
    }*/

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

   /* public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }*/

    public Map<String, Object> toHashMap(String key) {
        Map<String, Object> map = new HashMap<>();
        this.id = key;
        map.put("/task/" + id + "/id/", id);
        map.put("/task/" + id + "/name/", name);
        map.put("/task/" + id + "/description/", description);
        //map.put("/task/" + id + "/projectID/", projectID);
        map.put("/task/" + id + "/startDate/", startDate);
        map.put("/task/" + id + "/dueDate/", endDate);
       // map.put("/task/" + id + "/priority/", priority.toString());
        return map;
    }
}
