package com.example.popularmovies.retrofit;

import android.content.Context;

import com.example.popularmovies.R;
import com.example.popularmovies.retrofit.callbacks.RetrofitResultsCallback;
import com.example.popularmovies.retrofit.callbacks.RetrofitResult;
import com.example.popularmovies.retrofit.models.Movie;
import com.example.popularmovies.retrofit.models.MoviesList;
import com.example.popularmovies.retrofit.models.RequestResult;
import com.example.popularmovies.retrofit.models.Review;
import com.example.popularmovies.retrofit.models.ReviewsResults;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {
    //Constants
    private final static String BASE_URL = "http://api.themoviedb.org/3/movie/";

    //Variables
    private final String mApiKey;
    private final TheMovieDbService mService;
    private Call<MoviesList> mRequest;
    private RetrofitResultsCallback<MoviesList> moviesListCallback;

    //live data
    private MutableLiveData<RequestResult<List<Movie>>> mMoviesListLiveData = new MutableLiveData<>();
    private MutableLiveData<RequestResult<List<Review>>> mMovieReviewsLiveData = new MutableLiveData<>();

    private boolean mIsPopularRequestInProgress = false;
    private boolean mIsTopRatedRequestInProgress = false;

    public RetrofitUtil(Context context) {
        Retrofit retrofit = getRetrofit();
        mApiKey = getApiKey(context);
        mService = retrofit.create(TheMovieDbService.class);

        moviesListCallback = new RetrofitResultsCallback<>(new RetrofitResult<MoviesList>() {
            @Override
            public void onResult(MoviesList result) {
                List<Movie> moviesList = result.getMovies();
                RequestResult<List<Movie>> results = new RequestResult<>(moviesList);
                mMoviesListLiveData.postValue(results);
            }

            @Override
            public void onError(Throwable error) {
                RequestResult<List<Movie>> result = new RequestResult<>(error);
                mMoviesListLiveData.postValue(result);
            }
        });
    }

    private String getApiKey(Context context) {
        return context.getString(R.string.api_key);
    }

    public void requestPopularMovies() {
        if (mIsPopularRequestInProgress) {
            return;
        }

        if (mIsTopRatedRequestInProgress) {
            cancelCurrentRequest();
        }

        mRequest = mService.getPopularMovies(mApiKey);
        mRequest.enqueue(moviesListCallback);
        mIsPopularRequestInProgress = true;
    }

    public LiveData<RequestResult<List<Movie>>> getMoviesList() {
        return mMoviesListLiveData;
    }

    public void requestTopRatedMovies() {
        if (mIsTopRatedRequestInProgress) {
            return;
        }

        if (mIsPopularRequestInProgress) {
            cancelCurrentRequest();
        }

        Call<MoviesList> request = mService.getTopRatedMovies(mApiKey);
        request.enqueue(moviesListCallback);
        mIsTopRatedRequestInProgress = true;
    }

    public void requestReviewsById(long movieId){
        Call<ReviewsResults> request = mService.getReviewsById(mApiKey, movieId);

        Callback<ReviewsResults> reviewsCallback = new Callback<ReviewsResults>() {
            @Override
            public void onResponse(Call<ReviewsResults> call, Response<ReviewsResults> response) {

            }

            @Override
            public void onFailure(Call<ReviewsResults> call, Throwable t) {

            }
        };
        request.enqueue(reviewsCallback);
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
}
