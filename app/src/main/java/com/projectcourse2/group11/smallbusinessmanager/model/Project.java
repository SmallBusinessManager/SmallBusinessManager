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

    public Project(String id, String name, String description, String employeeID, Date startDate, Date dueDate) {
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
        this.id = key;
        map.put("/companyProjects/" + id + "/id/", getId());
        map.put("/companyProjects/" + id + "/name/", getName());
        map.put("/companyProjects/" + id + "/description/", getDescription());
        map.put("/companyProjects/" + id + "/employeeID/", getEmployeeID());
        map.put("/companyProjects/" + id + "/startDate/", getStartDate());
        map.put("/companyProjects/" + id + "/dueDate/", getDueDate());
        return map;
    }
}

