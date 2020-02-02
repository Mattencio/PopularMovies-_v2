package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.popularmovies.retrofit.models.Movie;
import com.example.popularmovies.viewmodel.MainViewModel;
import com.example.popularmovies.views.MovieDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import static com.example.popularmovies.views.MovieDetailsActivity.MOVIE_DETAILS;

public class MainActivity extends AppCompatActivity implements MoviesListAdapter.ItemTouchedListener {

    //Variables
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private MainViewModel mViewModel;
    private MoviesListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initViewModel();
        showPopularMovies();
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mViewModel.getMoviesList().observe(this, requestResult -> {
            if (requestResult.IsSuccessful) {
                List<Movie> result = requestResult.getResult();
                updateMoviesByList(result);
            } else {
                Throwable error = requestResult.getError();
                showError(error);
            }
        });

        mViewModel.getFavoritesMoviesItemByItem().observe(this, movieResult -> {
            if (movieResult.IsSuccessful) {
                Movie movie = movieResult.getResult();
                updateMoviesByItem(movie);
            } else {
                Throwable error = movieResult.getError();
                showError(error);
            }
        });
    }

    private void initUI() {
        mRecyclerView = findViewById(R.id.rv_movies_list);
        mProgressBar = findViewById(R.id.progress_bar);

        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int selectedOption = item.getItemId();

        switch (selectedOption) {
            case R.id.show_popular:
                showPopularMovies();
                break;
            case R.id.show_rated:
                showRatedMovies();
                break;
            case R.id.show_favorites:
                showFavoriteMovies();
                break;
            default:
        }

        return true;
    }

    private void showFavoriteMovies() {
        loadingMoviesUI();
        mAdapter = null;
        mViewModel.getFavoriteMoviesEntities().observe(this, movieEntities -> {
            if (movieEntities != null) {
                mViewModel.requestFavoriteMoviesByList(movieEntities);
            }
        });
    }

    private void showRatedMovies() {
        loadingMoviesUI();
        mViewModel.requestTopRatedMovies();
    }

    private void showPopularMovies() {
        loadingMoviesUI();
        mViewModel.requestPopularMovies();
    }

    private void loadingMoviesUI() {
        setLoadingVisibility(true);
        setRecyclerVisibility(false);
    }

    private void setRecyclerViewAdapter(MoviesListAdapter newAdapter) {
        setRecyclerVisibility(true);
        RecyclerView.Adapter recyclerAdapter = mRecyclerView.getAdapter();
        if (recyclerAdapter == null) {
            mRecyclerView.setAdapter(newAdapter);
        } else {
            mRecyclerView.swapAdapter(newAdapter, false);
        }
    }

    private void setLoadingVisibility(boolean isVisible) {
        int visibility = isVisible? View.VISIBLE : View.GONE;
        mProgressBar.setVisibility(visibility);
    }

    private void setRecyclerVisibility(boolean isVisible) {
        int visibility = isVisible? View.VISIBLE : View.GONE;
        mRecyclerView.setVisibility(visibility);
    }

    @Override
    public void onItemTouchedListener(Movie movie) {
        Context context = getApplicationContext();
        Class detailsActivityClass = MovieDetailsActivity.class;
        Intent intent = new Intent(context, detailsActivityClass);
        intent.putExtra(MOVIE_DETAILS, movie);
        startActivity(intent);
    }

    public void updateMoviesByList(List<Movie> moviesList) {
        mAdapter = new MoviesListAdapter(moviesList, MainActivity.this);
        setRecyclerViewAdapter(mAdapter);
        setLoadingVisibility(false);
    }

    public void updateMoviesByItem(Movie movie) {
        if (mAdapter == null) {
            List<Movie> movies = new ArrayList<>();
            movies.add(movie);
            mAdapter = new MoviesListAdapter(movies, MainActivity.this);
        } else {
            mAdapter.addMovie(movie);
        }
        setRecyclerViewAdapter(mAdapter);
        setLoadingVisibility(false);
    }

    public void showError(Throwable error) {
        error.printStackTrace();
        setLoadingVisibility(false);
        final String errorMessage = error.getMessage();
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}
