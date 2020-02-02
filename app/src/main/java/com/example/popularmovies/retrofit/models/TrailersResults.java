package com.example.popularmovies.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersResults {
    @Expose
    @SerializedName("results")
    private List<Trailer> trailersList;

    public List<Trailer> getTrailersList() {
        return trailersList;
    }
}
