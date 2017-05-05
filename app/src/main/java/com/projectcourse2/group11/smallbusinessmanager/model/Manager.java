package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.ArrayList;
import java.util.Map;

public class Manager extends Person {
    private ArrayList<TeamLeader> teamLeaders = new ArrayList<>();
    private ArrayList<Project> projects = new ArrayList<>();

    public Manager(String SSN, String firstName, String lastName, String phoneNumber, String email/*, Address address*/) {
        super(SSN, firstName, lastName, phoneNumber, email/*, address*/);
        setPosition(Position.MANAGER);
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
//    public Map<String, Object> toHashMap(){
//        Map<String, Object> map = super.toHashMap();
//        return super.toHashMap();
//    }
}
