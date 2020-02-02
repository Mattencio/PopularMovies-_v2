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
import com.example.popularmovies.retrofit.models.Trailer;
import com.example.popularmovies.retrofit.models.TrailersResults;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {
    //Constants
    private final static String BASE_URL = "http://api.themoviedb.org/3/movie/";

    //Variables
    private final String mApiKey;
    private final TheMovieDbService mService;
    private Call<MoviesList> mRequest;
    //Callbacks
    private RetrofitResultsCallback<MoviesList> mMoviesListCallback;
    private RetrofitResultsCallback<ReviewsResults> mReviewsListCallback;
    private RetrofitResultsCallback<TrailersResults> mTrailersListCallback;

    //live data
    private MutableLiveData<RequestResult<List<Movie>>> mMoviesListLiveData = new MutableLiveData<>();
    private MutableLiveData<RequestResult<List<Review>>> mMovieReviewsLiveData = new MutableLiveData<>();
    private MutableLiveData<RequestResult<List<Trailer>>> mMovieTrailersLiveData = new MutableLiveData<>();
    private MutableLiveData<RequestResult<Movie>> mMovieByItemLiveData = new MutableLiveData<>();

    private boolean mIsPopularRequestInProgress = false;
    private boolean mIsTopRatedRequestInProgress = false;

    public RetrofitUtil(Context context) {
        Retrofit retrofit = getRetrofit();
        mApiKey = getApiKey(context);
        mService = retrofit.create(TheMovieDbService.class);

        createCallbacks();
    }

    private void createCallbacks() {
        mMoviesListCallback = new RetrofitResultsCallback<>(new RetrofitResult<MoviesList>() {
            @Override
            public void onResult(MoviesList result) {
                List<Movie> moviesList = result.getMovies();
                RequestResult<List<Movie>> results = new RequestResult<>(moviesList);
                mMoviesListLiveData.postValue(results);
                requestFinished();
            }

            @Override
            public void onError(Throwable error) {
                RequestResult<List<Movie>> result = new RequestResult<>(error);
                mMoviesListLiveData.postValue(result);
                requestFinished();
            }
        });

        mReviewsListCallback = new RetrofitResultsCallback<>(new RetrofitResult<ReviewsResults>() {
            @Override
            public void onResult(ReviewsResults result) {
                List<Review> reviews = result.getReviews();
                RequestResult<List<Review>> results = new RequestResult<>(reviews);
                mMovieReviewsLiveData.postValue(results);
            }

            @Override
            public void onError(Throwable error) {
                RequestResult<List<Review>> result = new RequestResult<>(error);
                mMovieReviewsLiveData.postValue(result);
            }
        });

        mTrailersListCallback = new RetrofitResultsCallback<>(new RetrofitResult<TrailersResults>() {
            @Override
            public void onResult(TrailersResults result) {
                List<Trailer> trailers = result.getTrailersList();
                RequestResult<List<Trailer>> results = new RequestResult<>(trailers);
                mMovieTrailersLiveData.postValue(results);
            }

            @Override
            public void onError(Throwable error) {
                RequestResult<List<Trailer>> result = new RequestResult<>(error);
                mMovieTrailersLiveData.postValue(result);
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

        cancelCurrentRequest();

        mRequest = mService.getPopularMovies(mApiKey);
        mRequest.enqueue(mMoviesListCallback);
        mIsPopularRequestInProgress = true;
    }

    public LiveData<RequestResult<List<Movie>>> getMoviesList() {
        return mMoviesListLiveData;
    }

    public LiveData<RequestResult<List<Review>>> getReviews() {
        return mMovieReviewsLiveData;
    }

    public LiveData<RequestResult<List<Trailer>>> getTrailers() {
        return mMovieTrailersLiveData;
    }

    public LiveData<RequestResult<Movie>> getMovieByItem() {
        return mMovieByItemLiveData;
    }

    public void requestTopRatedMovies() {
        if (mIsTopRatedRequestInProgress) {
            return;
        }

        cancelCurrentRequest();

        Call<MoviesList> request = mService.getTopRatedMovies(mApiKey);
        request.enqueue(mMoviesListCallback);
        mIsTopRatedRequestInProgress = true;
    }

    public void requestReviewsById(long movieId){
        Call<ReviewsResults> request = mService.getReviewsById(movieId, mApiKey);
        request.enqueue(mReviewsListCallback);
    }

    public void requestMovieTrailersById(long movieId){
        Call<TrailersResults> request = mService.getTrailersById(movieId, mApiKey);
        request.enqueue(mTrailersListCallback);
    }

    public void requestMoviesDetailsByIds(List<Long> moviesIds) {
        cancelCurrentRequest();

        for (Long movieId : moviesIds) {
            Call<Movie> request = mService.getMovieDetailsById(movieId, mApiKey);
            request.enqueue(new RetrofitResultsCallback<>(new RetrofitResult<Movie>() {
                @Override
                public void onResult(Movie result) {
                    RequestResult<Movie> movieResult = new RequestResult<>(result);
                    mMovieByItemLiveData.setValue(movieResult);
                }

                @Override
                public void onError(Throwable error) {
                    RequestResult<Movie> result = new RequestResult<>(error);
                    mMovieByItemLiveData.postValue(result);
                }
            }));
        }
    }

    private void cancelCurrentRequest() {
        if (mRequest != null) {
            mRequest.cancel();
            requestFinished();
        }
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
