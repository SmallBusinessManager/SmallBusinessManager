package com.projectcourse2.group11.smallbusinessmanager.model;

import java.util.HashMap;
import java.util.Map;

public class Worker extends Person {
    private TeamLeader teamLeader;

    public Worker(){
    }
    public Worker(String ssn, String firstName, String lastName, String phoneNumber, String email, String UID) {
        super(ssn, firstName, lastName, phoneNumber, email, UID);
        setPosition(Position.WORKER);
    }

    public void viewOrders(){
        //TODO
    }

    public void viewWorkingHours(){
        //TODO
    }
    public TeamLeader getTeamLeader(){
        return teamLeader;
    }
    public void assignTeamLeader(TeamLeader teamLeader){
        this.teamLeader=teamLeader;
    }

    public Map<String, Object> toHashMap(){
        if (teamLeader!=null) {
            Map<String, Object> map;
            map = super.toHashMap();
            map.put(getSsn() + "/teamLeader/", teamLeader.getSsn());
            return map;
        } else {
            return super.toHashMap();
        }
    }
    public String toString(){
        return getSsn()+" "+getFirstName()+" "+getLastName();
    }
}
