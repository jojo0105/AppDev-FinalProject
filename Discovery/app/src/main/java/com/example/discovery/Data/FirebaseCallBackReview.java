package com.example.discovery.Data;

import com.example.discovery.Models.Review;

import java.util.List;

public interface FirebaseCallBackReview {
    void onReviewReponse(List<Review> reviews);
}
