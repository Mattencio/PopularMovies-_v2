package com.example.popularmovies.retrofit.models;

public class RequestResult<T> {
    private T mResult;
    public boolean IsSuccessful;
    private Throwable mError;

    public RequestResult(T movieList) {
        IsSuccessful = true;
        mResult = movieList;
    }

    public RequestResult(Throwable error) {
        IsSuccessful = false;
        mError = error;
    }

    public T getResult() {
        return mResult;
    }

    public Throwable getError() {
        return mError;
    }
}
