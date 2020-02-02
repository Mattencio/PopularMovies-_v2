package com.example.popularmovies.database.dao;

import com.example.popularmovies.database.tables.MovieEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MovieDao {

    @Insert
    void insertMovie(MovieEntity movie);

    @Delete
    void deleteMovie(MovieEntity movie);

    @Query("SELECT * FROM favorite_movies")
    LiveData<List<MovieEntity>> getFavoriteMovies();

    @Query("SELECT * FROM favorite_movies WHERE `key` = :movieKey")
    LiveData<MovieEntity> findMovieByKey(long movieKey);
}
