package com.example.popularmovies.viewmodel;

import android.app.Application;

import com.example.popularmovies.retrofit.RetrofitUtil;
import com.example.popularmovies.retrofit.models.Movie;
import com.example.popularmovies.retrofit.models.RequestResult;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {
    private LiveData<RequestResult<List<Movie>>> mMoviesList;
    private RetrofitUtil mRetrofitUtil;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRetrofitUtil = new RetrofitUtil(application);
        mMoviesList = mRetrofitUtil.getMoviesList();
    }

    public LiveData<RequestResult<List<Movie>>> getMoviesList(){
        return mMoviesList;
    }

    public void requestTopRatedMovies() {
        mRetrofitUtil.requestTopRatedMovies();
    }

    public void requestPopularMovies() {
        mRetrofitUtil.requestPopularMovies();
    }
}
