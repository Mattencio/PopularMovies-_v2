package com.example.popularmovies.database.utils;

import com.example.popularmovies.database.tables.MovieEntity;
import com.example.popularmovies.retrofit.models.Movie;

public class MovieToMovieEntityConverter {
    private MovieToMovieEntityConverter(){
    }

    public static MovieEntity convertMovie(Movie movie) {
        String name = movie.getTitle();
        Long key = movie.getId();
        return new MovieEntity(name, key);
    }
}
