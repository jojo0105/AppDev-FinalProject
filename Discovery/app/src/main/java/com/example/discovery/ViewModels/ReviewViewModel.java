package com.example.discovery.ViewModels;

import com.example.discovery.Data.ReviewDao;
import com.example.discovery.Models.Review;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Query;

public class ReviewViewModel {
    public static ReviewDao reviewDao = new ReviewDao();

    public static Task<Void> addReview(Review review){
        return reviewDao.addReview(review);
    }

    public static Query retrieveReview(String parkID){
        return reviewDao.retrieveReview(parkID);
    }

}
