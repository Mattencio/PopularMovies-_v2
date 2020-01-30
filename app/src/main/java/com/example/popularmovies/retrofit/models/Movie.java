package com.example.popularmovies.retrofit.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {
    @Expose
    @SerializedName("original_title")
    private String mTitle;
    @Expose
    @SerializedName("release_date")
    private String mReleaseDate;
    @Expose
    @SerializedName("poster_path")
    private String mPosterPath;
    @Expose
    @SerializedName("vote_average")
    private Float mVoteAverage;
    @Expose
    @SerializedName("overview")
    private String mSynopsis;
    @Expose
    @SerializedName("id")
    private Long mId;

    protected Movie(Parcel in) {
        mTitle = in.readString();
        mReleaseDate = in.readString();
        mPosterPath = in.readString();
        if (in.readByte() == 0) {
            mVoteAverage = null;
        } else {
            mVoteAverage = in.readFloat();
        }
        mSynopsis = in.readString();
        if (in.readByte() == 0) {
            mId = null;
        } else {
            mId = in.readLong();
        }
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return mTitle;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public Float getVoteAverage() {
        return mVoteAverage;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public Long getId() {
        return mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mReleaseDate);
        dest.writeString(mPosterPath);
        if (mVoteAverage == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(mVoteAverage);
        }
        dest.writeString(mSynopsis);
        if (mId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mId);
        }
    }
}