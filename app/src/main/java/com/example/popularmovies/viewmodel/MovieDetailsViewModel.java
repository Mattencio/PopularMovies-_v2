package com.example.popularmovies.viewmodel;

import android.app.Application;

import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.database.tables.MovieEntity;
import com.example.popularmovies.database.utils.AppExecutors;
import com.example.popularmovies.database.utils.MovieToMovieEntityConverter;
import com.example.popularmovies.retrofit.RetrofitUtil;
import com.example.popularmovies.retrofit.models.Movie;
import com.example.popularmovies.retrofit.models.RequestResult;
import com.example.popularmovies.retrofit.models.Review;
import com.example.popularmovies.retrofit.models.Trailer;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MovieDetailsViewModel extends AndroidViewModel {

    private LiveData<RequestResult<List<Review>>> mReviews;
    private LiveData<RequestResult<List<Trailer>>> mTrailers;
    private LiveData<MovieEntity> mFavoriteMovie;
    private RetrofitUtil mRetrofitUtil;
    private final AppDatabase mDatabase;

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        mRetrofitUtil = new RetrofitUtil(application);
        mReviews = mRetrofitUtil.getReviews();
        mTrailers = mRetrofitUtil.getTrailers();
        mDatabase = AppDatabase.getInstance(application);
    }

    public LiveData<RequestResult<List<Review>>> getReviews() {
        return mReviews;
    }

    public LiveData<RequestResult<List<Trailer>>> getTrailers() {
        return mTrailers;
    }

    public void requestMovieReviewsById(long movieId) {
        mRetrofitUtil.requestReviewsById(movieId);
    }

    public void requestMovieTrailersById(long movieId) {
        mRetrofitUtil.requestMovieTrailersById(movieId);
    }

    public void saveMovieAsFavorite(Movie movie) {
        MovieEntity movieEntity = MovieToMovieEntityConverter.convertMovie(movie);
        AppExecutors.getInstance().diskIO().execute(() -> mDatabase.movieDao().insertMovie(movieEntity));
    }

    public LiveData<MovieEntity> getFavoriteMovie(long movieKey) {
        if (mFavoriteMovie == null) {
            mFavoriteMovie = mDatabase.movieDao().findMovieByKey(movieKey);
        }
        return mFavoriteMovie;
    }

    public void deleteMovieAsFavorite() {
        MovieEntity movieEntity = mFavoriteMovie.getValue();
        AppExecutors.getInstance().diskIO().execute(() -> mDatabase.movieDao().deleteMovie(movieEntity));
    }
}
