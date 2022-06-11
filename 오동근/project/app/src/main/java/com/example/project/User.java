package com.example.project;

public class User {

    String restName, location, lastMsgTime;
    int imageId;

    public User(String restName, String location, String lastMsgTime, int imageId) {
        this.restName = restName;
        this.location = location;
        this.lastMsgTime = lastMsgTime;
        this.imageId = imageId;
    }
}
