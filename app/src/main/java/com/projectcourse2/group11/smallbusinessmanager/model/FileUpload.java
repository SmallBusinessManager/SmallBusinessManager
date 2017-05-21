package com.projectcourse2.group11.smallbusinessmanager.model;

public class FileUpload {
    private String name;
    private String uri;
    private String uid;

    public FileUpload(){}
    public FileUpload(String uid,String name, String uri) {
        this.uid=uid;
        this.name = name;
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }
}
