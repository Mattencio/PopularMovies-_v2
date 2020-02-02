package com.example.popularmovies.database.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_movies")
public class MovieEntity {
    //Id of element in the table
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "name")
    private String mName;

    //Movie Id from web API
    @ColumnInfo(name = "key")
    private long mKey;

    public MovieEntity(int id, String name, long key) {
        mId = id;
        mName = name;
        mKey = key;
    }

    @Ignore
    public MovieEntity(String name, long key) {
        mName = name;
        mKey = key;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public long getKey() {
        return mKey;
    }
}
