package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Project {
    private UUID id;
    private String name;
    private String description;
    private String employeeID;
    private Date startDate;
    private Date dueDate;

    public Project(String name, String description, String employeeID, Date startDate, Date dueDate) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.employeeID = employeeID;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    public UUID getId() {
        return id;
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

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Map<String, Object> toHashMap(String key) {
        Map<String, Object> map = new HashMap<>();
        map.put(id + "/id/", getId());
        map.put(id + "/name/", getName());
        map.put(id+ "/description/", getDescription());
        map.put(id + "/employeeID/", getEmployeeID());
        map.put(id + "/startDate/", getStartDate());
        map.put(id + "/dueDate/", getDueDate());
        return map;
    }
}

