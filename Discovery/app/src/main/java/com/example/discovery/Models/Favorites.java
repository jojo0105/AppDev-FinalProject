package com.example.discovery.Models;

import java.util.List;

public class Favorites {
    private String userId;
    private String parkId;
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

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }
}
