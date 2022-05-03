package com.example.discovery.Data;

import com.example.discovery.Models.Park;

import java.util.ArrayList;

public interface FirebaseCallBack {
    void onResponse(ArrayList<Park> allFav);
}
