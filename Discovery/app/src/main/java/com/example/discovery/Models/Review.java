package com.example.discovery.Models;

public class Review {
    private String comment;
    private String userName;
    private String parkID;

    public Review() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getParkID() { return parkID; }

    public void setParkID(String parkID) { this.parkID = parkID; }
}
