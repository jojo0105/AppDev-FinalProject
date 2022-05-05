package com.example.discovery.Models;


import java.text.SimpleDateFormat;
import java.util.Date;

public class Visit {
    private String id;
    private Park park;
    private Date date;
    private String userId;
    private String note;
    private Boolean status;
//    private Timestamp timestamp;

    public Visit() {
    }

    public Park getPark() {
        return park;
    }

    public void setPark(Park park) {
        this.park = park;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

//    public Timestamp getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(Timestamp timestamp) {
//        this.timestamp = timestamp;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateString(){
        SimpleDateFormat formatter = new SimpleDateFormat("E, MMM dd yyyy");
        return formatter.format(date);
    }


}
