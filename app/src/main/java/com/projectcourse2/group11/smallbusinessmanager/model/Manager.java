package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Manager extends Person {
    private ArrayList<TeamLeader> teamLeaders = new ArrayList<>();
    private ArrayList<Project> projects = new ArrayList<>();

    public Manager(String SSN, String firstName, String lastName, String phoneNumber, String email, Address address) {
        super(SSN, firstName, lastName, phoneNumber, email, address);
    }

    public void createWorkOrders(){
        //to to
    }

    public ArrayList getProject(){
        return this.projects;
    }
    public void addTeamleader(TeamLeader teamLeader){
        teamLeaders.add(teamLeader);
    }
    public void addProject(Project project){
        projects.add(project);
    }

    public ArrayList getTeamLeader(){
        return this.teamLeaders;
    }

    public void addExpense(){
        //to to
    }

    public void approvePayment(){
        //to to
    }
    public Map<String, Object> toHashMap(){
        Map<String, Object> map;
        if (teamLeaders!=null) {
            map = super.toHashMap();
            int i = 1;
            for(TeamLeader tl: teamLeaders){
                map.put("/worker/"+getSSN()+"/teamLeader/tl"+i+"/",tl.getSSN());
                i++;
            }
            i=1;
            for (Project project: projects){
                map.put("/worker/"+getSSN()+"/project/+project"+i+"/",project);
                i++;
            }
            map.put("/worker/" + getSSN() + "/isManager/",true);
            return map;
        } else {
            return super.toHashMap();
        }
    }
}
