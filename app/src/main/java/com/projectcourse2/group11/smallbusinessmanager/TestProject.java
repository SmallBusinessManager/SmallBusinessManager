package com.projectcourse2.group11.smallbusinessmanager;

import java.util.HashMap;
import java.util.Map;

public class TestProject {
    private String id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;

    public TestProject(String name, String description, Date startDate, Date endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public Map<String, Object> toHashMap(String key) {
        Map<String, Object> map = new HashMap<>();
        this.id=key;
        //map.put("/project/"+id+"/id/",id);
        map.put("/project/" + id + "/name/", name);
        map.put("/project/" + id + "/description/", description);
        map.put("/project/" + id + "/startDate/", startDate);
        map.put("/project/" + id + "/dueDate/", endDate);
        return map;
    }
}

