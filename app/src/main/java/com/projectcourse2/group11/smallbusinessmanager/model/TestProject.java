package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.HashMap;
import java.util.Map;

public class TestProject {
    public String id;
    public String name;
    public String description;
    public Date startDate;
    public Date endDate;

    /*Blank default constructor essential for Firebase*/
    public TestProject() {
    }

    public TestProject(String id, String name, String description, Date startDate, Date endDate) {
        this.id = id;
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
        this.id = key;
        map.put("/companyProjects/company1/" + id + "/id/", id);
        map.put("/companyProjects/company1/" + id + "/name/", name);
        map.put("/companyProjects/company1/" + id + "/description/", description);
        map.put("/companyProjects/company1/" + id + "/startDate/", startDate);
        map.put("/companyProjects/company1/" + id + "/endDate/", endDate);
        return map;
    }

    @Override
    public String toString() {
        return name + description;
    }
}

