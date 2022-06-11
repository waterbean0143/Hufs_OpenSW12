package com.example.project1;

public class User {

    String restName, location, lastMsgTime, menu, price;
    int imageId;

    public User(String restName, String location, String lastMsgTime, String menu, String price, int imageId) {
        this.restName = restName;
        this.location = location;
        this.lastMsgTime = lastMsgTime;
        this.menu = menu;
        this.price = price;
        this.imageId = imageId;
    }
}
