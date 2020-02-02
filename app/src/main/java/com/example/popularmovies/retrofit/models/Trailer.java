package com.example.popularmovies.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Trailer implements Serializable {
    @Expose
    @SerializedName("key")
    private String mKey;
    @Expose
    @SerializedName("name")
    private String mName;
    @Expose
    @SerializedName("site")
    private String mSite;

    public String getKey() {
        return mKey;
    }

    public String getName() {
        return mName;
    }

    public String getSite() {
        return mSite;
    }
}
