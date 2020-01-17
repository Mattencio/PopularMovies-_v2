package com.example.popularmovies.picasso;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoUtil {
    //Constants
    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String DEFAULT_IMAGE_SIZE = "w185/";

    public void drawImageFromUrl(String url, ImageView imageView) {
        final String completeUrl = BASE_URL + DEFAULT_IMAGE_SIZE + url;
        Picasso.get().load(completeUrl).into(imageView);
    }
}
