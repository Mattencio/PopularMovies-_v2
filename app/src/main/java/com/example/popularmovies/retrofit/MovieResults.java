package com.example.popularmovies.retrofit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResults {
    @Expose
    @SerializedName("results")
    private List<Movie> mMovies;

    List<Movie> getMovies() {
        return mMovies;
    }
}
