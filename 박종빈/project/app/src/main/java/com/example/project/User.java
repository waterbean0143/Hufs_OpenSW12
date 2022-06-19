package com.example.project;

//listview를 위한 목록 리스트 생성
public class User {

    String restName, location, lastMsgTime; // 휴게소 명, 위치, 일련번호 
    int imageId; //이미지 주소

    public User(String restName, String location, String lastMsgTime, int imageId) {
        this.restName = restName;
        this.location = location;
        this.lastMsgTime = lastMsgTime;
        this.imageId = imageId;
    }

    public User() {

    }
}
