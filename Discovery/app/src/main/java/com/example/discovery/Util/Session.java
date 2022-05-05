package com.example.discovery.Util;

public class Session extends AppController{

    private String userId;
    private String userName;
    private String email;
    private static Session instance;

    public static Session getInstance(){
        if(instance == null)
            instance = new Session();
        return instance;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
