package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmovies.picasso.PicassoUtil;
import com.example.popularmovies.retrofit.Movie;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MoviesListViewHolder> {

    //Variables
    private final List<Movie> mMoviesList;
    private final ItemTouchedListener mOnTouchListener;
    private final PicassoUtil mPicassoUtil;

    //Callbacks
    public interface ItemTouchedListener {
        void onItemTouchedListener(Movie movie);
    }

    public MoviesListAdapter(List<Movie> moviesListUrls, ItemTouchedListener listener) {
        mMoviesList = moviesListUrls;
        mOnTouchListener = listener;
        mPicassoUtil = new PicassoUtil();
    }

    @NonNull
    @Override
    public MoviesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutIdItem = R.layout.movie_item;
        View view = inflater.inflate(layoutIdItem, parent, false);
        return new MoviesListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesListViewHolder holder, int position) {
        final Movie movie = mMoviesList.get(position);
        holder.bindMovie(movie);
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    class MoviesListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private Movie mMovie;

        MoviesListViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_item);
            itemView.setOnClickListener(this);
        }

        void bindMovie(Movie movie) {
            mMovie = movie;
            final String url = movie.getPosterPath();
            mPicassoUtil.drawImageFromUrl(url, mImageView);
        }

        @Override
        public void onClick(View v) {
            if (mOnTouchListener != null && mMovie != null) {
                mOnTouchListener.onItemTouchedListener(mMovie);
            }
        }
    }
}
