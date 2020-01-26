package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.popularmovies.retrofit.models.Movie;
import com.example.popularmovies.viewmodel.MainViewModel;

import java.util.List;

import static com.example.popularmovies.MovieDetailsActivity.MOVIE_DETAILS;

public class MainActivity extends AppCompatActivity implements MoviesListAdapter.ItemTouchedListener {

    //Variables
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private MainViewModel mViewModel;

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
                updateMoviesRecyclerUI(result);
            } else {
                Throwable error = requestResult.getError();
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
            default:
        }

        return true;
    }

    private void showRatedMovies() {
        setLoadingVisibility(true);
        setRecyclerVisibility(false);
        mViewModel.requestTopRatedMovies();
    }

    private void showPopularMovies() {
        setLoadingVisibility(true);
        setRecyclerVisibility(false);
        mViewModel.requestPopularMovies();

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

    public void updateMoviesRecyclerUI(List<Movie> moviesList) {
        final MoviesListAdapter adapter = new MoviesListAdapter(moviesList, MainActivity.this);
        setRecyclerViewAdapter(adapter);
        setLoadingVisibility(false);
    }

    public void showError(Throwable error) {
        error.printStackTrace();
        setLoadingVisibility(false);
        final String errorMessage = error.getMessage();
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}
