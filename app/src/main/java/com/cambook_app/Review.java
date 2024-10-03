package com.cambook_app;

public class Review {
    private String userId;
    private String fullName;
    private String reviewText;

    // Required default constructor for Firebase
    public Review() {
    }

    public Review(String userID, String fullName, String reviewText) {
        this.userId = userID;
        this.fullName = fullName;
        this.reviewText = reviewText;
    }

    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getReviewText() {
        return reviewText;
    }
}

