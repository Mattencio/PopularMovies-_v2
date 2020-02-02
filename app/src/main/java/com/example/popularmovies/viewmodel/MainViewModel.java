package com.example.popularmovies.viewmodel;

import android.app.Application;

import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.database.tables.MovieEntity;
import com.example.popularmovies.retrofit.RetrofitUtil;
import com.example.popularmovies.retrofit.models.Movie;
import com.example.popularmovies.retrofit.models.RequestResult;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {
    private LiveData<RequestResult<List<Movie>>> mMoviesList;
    private LiveData<RequestResult<Movie>> mFavoriteMovie;
    private RetrofitUtil mRetrofitUtil;
    private AppDatabase mDatabase;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRetrofitUtil = new RetrofitUtil(application);
        mMoviesList = mRetrofitUtil.getMoviesList();
        mFavoriteMovie = mRetrofitUtil.getMovieByItem();
        mDatabase = AppDatabase.getInstance(application);
    }

    public LiveData<RequestResult<List<Movie>>> getMoviesList() {
        return mMoviesList;
    }

    public LiveData<List<MovieEntity>> getFavoriteMoviesEntities() {
        return mDatabase.movieDao().getFavoriteMovies();
    }

    public LiveData<RequestResult<Movie>> getFavoritesMoviesItemByItem() {
        return mFavoriteMovie;
    }

    public void requestTopRatedMovies() {
        mRetrofitUtil.requestTopRatedMovies();
    }

    public void requestPopularMovies() {
        mRetrofitUtil.requestPopularMovies();
    }

    public void requestFavoriteMoviesByList(List<MovieEntity> movieEntities) {
        List<Long> moviesIds = new ArrayList<>();
        for (MovieEntity movieEntity : movieEntities) {
            moviesIds.add(movieEntity.getKey());
        }
        mRetrofitUtil.requestMoviesDetailsByIds(moviesIds);
    }
}
