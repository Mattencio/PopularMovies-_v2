package com.example.popularmovies.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.picasso.PicassoUtil;
import com.example.popularmovies.retrofit.models.Movie;
import com.example.popularmovies.retrofit.models.Review;
import com.example.popularmovies.retrofit.models.Trailer;
import com.example.popularmovies.viewmodel.MovieDetailsViewModel;

import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {
    //Constants
    public final static String MOVIE_DETAILS = "MOVIE_DETAILS";

    private Movie mMovie;
    private MovieDetailsViewModel mViewModel;
    private boolean mIsFavoriteMovie = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mMovie = getIntent().getParcelableExtra(MOVIE_DETAILS);

        if (mMovie == null) {
            finish();
        }

        initViewModel();
        initUI();
        populateUI();
    }

    private void initUI() {
        Button buttonShowReviews = findViewById(R.id.button_show_reviews);
        Button buttonShowTrailers = findViewById(R.id.button_show_trailers);
        ImageButton buttonFavorite = findViewById(R.id.button_favorite);

        mViewModel.getReviews().observe(this, reviewsResult -> {
            if (reviewsResult.IsSuccessful){
                List<Review> reviews = reviewsResult.getResult();
                ReviewsDialogFragment dialog = ReviewsDialogFragment.newInstance(reviews);
                FragmentManager fragmentManager = getSupportFragmentManager();
                dialog.show(fragmentManager, "reviews");
            }
        });

        mViewModel.getTrailers().observe(this, trailersResult -> {
            if (trailersResult.IsSuccessful) {
                List<Trailer> trailers = trailersResult.getResult();
                TrailersDialogFragment dialog = TrailersDialogFragment.newInstance(trailers);
                FragmentManager fragmentManager = getSupportFragmentManager();
                dialog.show(fragmentManager, "trailers");
            }
        });

        mViewModel.getFavoriteMovie(mMovie.getId()).observe(this, movie -> {
            mIsFavoriteMovie = movie != null;
            setFavoriteImageIcon(buttonFavorite);
        });

        buttonShowReviews.setOnClickListener(v -> {
            long movieId = mMovie.getId();
            mViewModel.requestMovieReviewsById(movieId);
        });

        buttonShowTrailers.setOnClickListener(v -> {
            long movieId = mMovie.getId();
            mViewModel.requestMovieTrailersById(movieId);
        });

        buttonFavorite.setOnClickListener(v -> {
            mIsFavoriteMovie = !mIsFavoriteMovie;
            if (mIsFavoriteMovie) {
                mViewModel.saveMovieAsFavorite(mMovie);
            } else {
                mViewModel.deleteMovieAsFavorite();
            }
            setFavoriteImageIcon(buttonFavorite);
        });
    }

    private void setFavoriteImageIcon(ImageButton buttonFavorite) {
        int icon = mIsFavoriteMovie ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border;
        buttonFavorite.setImageResource(icon);
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
