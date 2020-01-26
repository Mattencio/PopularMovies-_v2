package com.example.popularmovies.retrofit.callbacks;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitResultsCallback<RetrofitType> implements Callback<RetrofitType> {
    private static final String ERROR_MESSAGE = "Could not get movies list";

    private final RetrofitResult<RetrofitType> mMoviesListResult;

    public RetrofitResultsCallback(RetrofitResult<RetrofitType> moviesListResult) {
        mMoviesListResult = moviesListResult;
    }

    @Override
    public void onResponse(Call<RetrofitType> call, Response<RetrofitType> response) {
        if (response.isSuccessful()) {
            RetrofitType moviesList;
            if (response.body() != null) {
                moviesList = response.body();
                mMoviesListResult.onResult(moviesList);
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
    public void onFailure(Call<RetrofitType> call, Throwable error) {
        mMoviesListResult.onError(error);
    }
}
