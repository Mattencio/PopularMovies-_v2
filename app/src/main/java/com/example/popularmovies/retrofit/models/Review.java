package com.example.popularmovies.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {
    @Expose
    @SerializedName("author")
    private String mAuthor;
    @Expose
    @SerializedName("content")
    private String mContent;
}
