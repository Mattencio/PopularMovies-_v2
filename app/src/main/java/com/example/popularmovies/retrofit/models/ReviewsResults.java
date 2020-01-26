package com.example.popularmovies.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResults {
    @Expose
    @SerializedName("results")
    private List<Review> mReviews;
}
