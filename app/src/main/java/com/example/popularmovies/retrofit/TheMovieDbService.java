package com.example.popularmovies.retrofit;

import com.example.popularmovies.retrofit.models.MoviesList;
import com.example.popularmovies.retrofit.models.ReviewsResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface TheMovieDbService {
    @GET("popular?")
    Call<MoviesList> getPopularMovies(@Query("api_key") String key);

    @GET("top_rated?")
    Call<MoviesList> getTopRatedMovies(@Query("api_key") String key);

    @GET("{id}/reviews?")
    Call<ReviewsResults> getReviewsById(@Path("id") Long id, @Query("api_key") String key);
}
