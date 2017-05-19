package com.projectcourse2.group11.smallbusinessmanager.model;

public class FileUpload {
    private String name;
    private String uri;

    public FileUpload(){}
    public FileUpload(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
