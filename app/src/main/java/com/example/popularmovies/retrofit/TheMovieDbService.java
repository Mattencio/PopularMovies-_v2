package com.example.popularmovies.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface TheMovieDbService {
    @GET("3/movie/popular?")
    Call<MovieResults> getPopularMovies(@Query("api_key") String key);

    @GET("3/movie/top_rated?")
    Call<MovieResults> getTopRatedMovies(@Query("api_key") String key);
}
