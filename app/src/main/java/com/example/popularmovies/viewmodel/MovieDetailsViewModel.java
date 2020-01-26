package com.example.popularmovies.viewmodel;

import android.app.Application;

import com.example.popularmovies.retrofit.RetrofitUtil;
import com.example.popularmovies.retrofit.models.RequestResult;
import com.example.popularmovies.retrofit.models.Review;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MovieDetailsViewModel extends AndroidViewModel {

    private LiveData<RequestResult<List<Review>>> mReviews;
    private RetrofitUtil mRetrofitUtil;

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        mRetrofitUtil = new RetrofitUtil(application);
        mReviews = mRetrofitUtil.getReviews();
    }

    public LiveData<RequestResult<List<Review>>> getReviews() {
        return mReviews;
    }

    public void requestMovieReviewsById(long movieId) {
        mRetrofitUtil.requestReviewsById(movieId);
    }
}
