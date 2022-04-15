package com.example.discovery.Util;

import android.app.Application;

public class Session extends AppController{

    private String userId;
    private String userName;
    private static Session instance;

    public static Session getInstance(){
        if(instance == null)
            instance = new Session();
        return instance;
    }

    public Session(){
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
