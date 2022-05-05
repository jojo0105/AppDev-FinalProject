package com.example.discovery.Data;

import com.example.discovery.Models.Review;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ReviewDao {
    private DatabaseReference databaseReference;

    public ReviewDao(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Review.class.getSimpleName());
    }

    public Task<Void> addReview(Review review){
        return databaseReference.push().setValue(review);
    }

    public Query retrieveReview(String parkID){
       return databaseReference.orderByChild("parkID").equalTo(parkID);
    }
}
