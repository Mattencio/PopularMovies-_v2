package com.example.popularmovies.retrofit.callbacks;

import com.example.popularmovies.retrofit.models.Movie;

import java.util.List;

public interface RetrofitResult {
    interface MoviesListResult {
        void onMoviesListResult(List<Movie> moviesList);
        void onError(Throwable error);
    }
}
