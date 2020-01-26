package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.picasso.PicassoUtil;
import com.example.popularmovies.retrofit.models.Movie;
import com.example.popularmovies.viewmodel.MovieDetailsViewModel;

public class MovieDetailsActivity extends AppCompatActivity {
    //Constants
    public final static String MOVIE_DETAILS = "MOVIE_DETAILS";

    //Variables
    private Movie mMovie;
    private MovieDetailsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mMovie = getIntent().getParcelableExtra(MOVIE_DETAILS);

        if (mMovie == null) {
            finish();
        }

        populateUI();
        initViewModel();
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);
    }

    private void populateUI() {
        final ImageView posterIv = findViewById(R.id.iv_movie_poster);
        final TextView titleTv = findViewById(R.id.tv_title);
        final TextView releaseDateTv = findViewById(R.id.tv_release_date);
        final TextView voteAverageTv = findViewById(R.id.tv_vote_average);
        final TextView synopsisTv = findViewById(R.id.tv_synopsis);

        final String posterUrl = mMovie.getPosterPath();
        final String titleText = mMovie.getTitle();
        final String releaseDateText = mMovie.getReleaseDate();
        final Float voteAverage = mMovie.getVoteAverage();
        final String voteAverageText = String.valueOf(voteAverage);
        final String synopsisText = mMovie.getSynopsis();

        titleTv.setText(titleText);
        releaseDateTv.setText(releaseDateText);
        voteAverageTv.setText(voteAverageText);
        synopsisTv.setText(synopsisText);

        new PicassoUtil().drawImageFromUrl(posterUrl, posterIv);
    }
}
