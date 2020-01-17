package com.example.popularmovies.retrofit;

import android.content.Context;

import com.example.popularmovies.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil implements Callback<MovieResults> {
    //Constants
    private final static String BASE_URL = "http://api.themoviedb.org/";
    private static final String ERROR_MESSAGE = "Could not get movies list";

    //Variables
    private final String mApiKey;
    private final TheMovieDbService mService;
    private MoviesListResult mMoviesListCallback;
    private Call<MovieResults> mRequest;

    private boolean mIsPopularRequestInProgress = false;
    private boolean mIsTopRatedRequestInProgress = false;

    //Callbacks
    public interface MoviesListResult {
        void onMoviesListResult(List<Movie> moviesList);
        void onError(Throwable error);
    }

    public RetrofitUtil(Context context) {
        Retrofit retrofit = getRetrofit();
        mApiKey = getApiKey(context);
        mService = retrofit.create(TheMovieDbService.class);
    }

    private String getApiKey(Context context) {
        return context.getString(R.string.api_key);
    }

    public void enqueuePopularMovies(final MoviesListResult callback) {
        if (callback == null || mIsPopularRequestInProgress) {
            return;
        }

        if (mIsTopRatedRequestInProgress) {
            cancelCurrentRequest();
        }

        mMoviesListCallback = callback;
        mRequest = mService.getPopularMovies(mApiKey);
        mRequest.enqueue(this);
        mIsPopularRequestInProgress = true;
    }

    public void enqueueTopRatedMovies(final MoviesListResult movieListResultCallback) {
        if (movieListResultCallback == null || mIsTopRatedRequestInProgress) {
            return;
        }

        if (mIsPopularRequestInProgress) {
            cancelCurrentRequest();
        }

        mMoviesListCallback = movieListResultCallback;
        Call<MovieResults> request = mService.getTopRatedMovies(mApiKey);
        request.enqueue(this);
        mIsTopRatedRequestInProgress = true;
    }

    private void cancelCurrentRequest() {
        mRequest.cancel();
        requestFinished();
    }

    private Retrofit getRetrofit() {
        Gson gson = getGson();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    private Gson getGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    private void requestFinished() {
        mIsPopularRequestInProgress = false;
        mIsTopRatedRequestInProgress = false;
    }

    @Override
    public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
        requestFinished();

        if (response.isSuccessful()) {
            List<Movie> moviesList;
            if (response.body() != null) {
                moviesList = response.body().getMovies();
                mMoviesListCallback.onMoviesListResult(moviesList);
            } else {
                Throwable error = new RuntimeException(ERROR_MESSAGE);
                mMoviesListCallback.onError(error);
            }

        } else {
            int code = response.code();
            String message = response.message();
            String errorMessage = "Code: " + code + " - " + message;
            Throwable error = new RuntimeException(errorMessage);
            mMoviesListCallback.onError(error);
        }
    }

    @Override
    public void onFailure(Call<MovieResults> call, Throwable error) {
        requestFinished();
        mMoviesListCallback.onError(error);
    }
}
