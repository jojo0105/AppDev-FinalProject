package com.example.discovery.Data;

import android.util.Log;

import com.example.discovery.Models.Favorites;
import com.example.discovery.Models.Park;
import com.example.discovery.Models.Visit;
import com.example.discovery.Util.DB;
import com.example.discovery.Util.Session;
import com.google.firebase.firestore.CollectionReference;

public class VisiteDao {
    private  static CollectionReference visitModel = DB.selectCollection("visit");



    public static void addToViste(Visit visit) {
        visitModel.add(visit).addOnSuccessListener(documentReference -> Log.d("AddToVisit_Click", "added"))
                .addOnFailureListener(e -> Log.d("AddToVisit_Click", e.toString()));

    }
}
