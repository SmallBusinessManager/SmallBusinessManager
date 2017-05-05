package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.HashMap;
import java.util.Map;

public class TestProject {
    private String name;
    private String description;
    private String startDate;
    private String endDate;

    public TestProject(String name, String description, String startDate, String endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

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
        return map;
    }
}
