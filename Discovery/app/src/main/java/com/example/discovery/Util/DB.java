package com.example.discovery.Util;

import com.example.discovery.Models.Favorites;
import com.example.discovery.Models.Review;
import com.example.discovery.Models.Visit;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DB {
    private static FirebaseDatabase db = FirebaseDatabase.getInstance();;
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static DatabaseReference selectCollection(String types){
        DatabaseReference databaseReference = null;
        switch (types.toLowerCase()){
            case "favorites":
                databaseReference = db.getReference(Favorites.class.getSimpleName());
                break;
            case "visit":
                databaseReference = db.getReference(Visit.class.getSimpleName());
                break;
            case "review":
                databaseReference = db.getReference(Review.class.getSimpleName());
                break;
        }
        return databaseReference;
    }

    public static CollectionReference selectCollection(){
        return firestore.collection("users");
    }
}
