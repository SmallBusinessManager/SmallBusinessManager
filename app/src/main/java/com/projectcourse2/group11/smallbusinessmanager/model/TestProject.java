package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.HashMap;
import java.util.Map;
<<<<<<< HEAD
import java.util.Random;
import java.util.UUID;
=======
>>>>>>> 32b6a50a5df1609ee2bc7f6c056d028cf290a219

public class TestProject {
    private UUID id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;

    public TestProject(String name, String description, Date startDate, Date endDate) {
        this.id= UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

<<<<<<< HEAD
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

    public Map<String,Object> toHashMap(){
        Map<String,Object> map = new HashMap<>();
        //map.put("/project/"+id+"/id/",id.toString());
        map.put("/project/"+id+"/name/",name);
        map.put("/project/"+id+"/description/",description);
        map.put("/project/"+id+"/startDate/",startDate.toString());
        map.put("/project/"+id+"/dueDate/",endDate.toString());
=======
    /**
     * This allow the simple object writing
     * such as:
     * databaseReference.updateChildren(testProject);
     */
    public Map<String,Object> toHashMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("/project/"+name+"/name/",name);
        map.put("/project/"+name+"/description/",description);
        map.put("/project/"+name+"/startDate/",startDate);
        map.put("project/"+name+"/endDate/",endDate);
>>>>>>> 32b6a50a5df1609ee2bc7f6c056d028cf290a219
        return map;
    }
}
