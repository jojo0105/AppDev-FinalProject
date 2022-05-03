package com.example.discovery.Models;

import java.sql.Timestamp;
import java.util.Date;

public class Visit {
    private Park park;
    private String date;
    private String userId;
    private String note;

    public Visit() {
    }

    public Park getPark() {
        return park;
    }

    public void setPark(Park park) {
        this.park = park;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
}
