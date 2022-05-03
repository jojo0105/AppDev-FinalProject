package com.example.discovery.Models;

import java.util.List;

public class Favorites {
    private String userId;
    private Park park;
    private List<String> getAll;

    public Favorites() {
    }

    public List<String> getGetAll() {
        return getAll;
    }

    public void setGetAll(List<String> getAll) {
        this.getAll = getAll;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Park getPark() {
        return park;
    }

    public void setPark(Park park) {
        this.park = park;
    }
}
