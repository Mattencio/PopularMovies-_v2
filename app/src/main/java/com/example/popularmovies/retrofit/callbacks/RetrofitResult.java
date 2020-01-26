package com.example.popularmovies.retrofit.callbacks;

public interface RetrofitResult<T> {
    void onResult(T result);
    void onError(Throwable error);
}
