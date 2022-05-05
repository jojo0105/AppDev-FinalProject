package com.example.discovery.Data;

import com.example.discovery.Models.Park;

import java.util.List;

public interface FirebaseCallBackPark {
    void onParkResponse(List<Park> allFav);
}
