package com.example.popularmovies.retrofit.callbacks;

import com.example.popularmovies.retrofit.models.Movie;
import com.example.popularmovies.retrofit.models.MoviesList;
import com.example.popularmovies.retrofit.callbacks.RetrofitResult.MoviesListResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesResultsCallback implements Callback<MoviesList> {
    private static final String ERROR_MESSAGE = "Could not get movies list";

    private final MoviesListResult mMoviesListResult;

    public MoviesResultsCallback(MoviesListResult moviesListResult) {
        mMoviesListResult = moviesListResult;
    }

    @Override
    public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
        if (response.isSuccessful()) {
            List<Movie> moviesList;
            if (response.body() != null) {
                moviesList = response.body().getMovies();
                mMoviesListResult.onMoviesListResult(moviesList);
            } else {
                Throwable error = new RuntimeException(ERROR_MESSAGE);
                mMoviesListResult.onError(error);
            }

        } else {
            int code = response.code();
            String message = response.message();
            String errorMessage = "Code: " + code + " - " + message;
            Throwable error = new RuntimeException(errorMessage);
            mMoviesListResult.onError(error);
        }
    }

    @Override
    public void onFailure(Call<MoviesList> call, Throwable error) {
        mMoviesListResult.onError(error);
    }
}
