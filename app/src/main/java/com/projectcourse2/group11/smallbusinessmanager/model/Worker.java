package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.HashMap;
import java.util.Map;

public class Worker extends Person {
    private TeamLeader teamLeader;

    public Worker(){
    }
    public Worker(String SSN, String firstName, String lastName, String phoneNumber, String email/*, Address address*/) {
        super(SSN, firstName, lastName, phoneNumber, email/*, address*/);
        setPosition(Position.WORKER);
    }

    public void viewOrders(){
        //to do
    }

    public void viewWorkingHours(){
        //to do
    }
    public TeamLeader getTeamLeader(){
        return teamLeader;
    }
    public void assignTeamLeader(TeamLeader teamLeader){
        this.teamLeader=teamLeader;
    }

    public Map<String, Object> toHashMap(){
        if (teamLeader!=null) {
            Map<String, Object> map = new HashMap<>();
            map = super.toHashMap();
            map.put("/worker/" + getSSN() + "/teamLeader/", teamLeader.getSSN());
            return map;
        } else {
            return super.toHashMap();
        }
    }
    public String toString(){
        return getSSN()+" "+getFirstName()+" "+getLastName();
    }
}
