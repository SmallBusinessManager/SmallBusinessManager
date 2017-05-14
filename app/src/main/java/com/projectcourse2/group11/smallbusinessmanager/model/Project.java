package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Project {
    private String id;
    private String name;
    private String description;
    private String employeeID;
    private Date startDate;
    private Date dueDate;
    public Project(){}


    /**
     * @param name name of the project
     * @param employeeID employee who can see the project
     */

    public Project(String name, String description, String employeeID, Date startDate, Date dueDate) {
        this.id = String.valueOf(UUID.randomUUID());
        this.name = name;
        this.description = description;
        this.employeeID = employeeID;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    /**
     * @param id To edit the read project
     * @param name name of the project
     * @param employeeID employee who can see the project
     */
    public Project(String id,String name, String description, String employeeID, Date startDate, Date dueDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.employeeID = employeeID;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    public String getId() {
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
        map.put(id + "/manager/", getEmployeeID());
        map.put(id + "/startDate/", getStartDate());
        map.put(id + "/dueDate/", getDueDate());
        return map;
    }
}

