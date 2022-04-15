package com.example.discovery.Util;

import android.widget.Switch;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class DB {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static CollectionReference selectCollection(String types){
        CollectionReference collectionReference = null;

        switch (types.toLowerCase()){
            case "users":
                collectionReference = db.collection("Users");
                break;
            case "favorites":
                collectionReference = db.collection("Favorites");

        }
        return collectionReference;
    }
}
