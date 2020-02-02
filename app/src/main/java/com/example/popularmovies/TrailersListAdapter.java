package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmovies.retrofit.models.Trailer;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrailersListAdapter extends RecyclerView.Adapter<TrailersListAdapter.ReviewsListViewHolder> {
    private List<Trailer> mTrailers;
    private OnMovieTrailerListener mOnMovieTrailerListener;

    public TrailersListAdapter(List<Trailer> reviews, OnMovieTrailerListener onMovieTrailerListener) {
        mTrailers = reviews;
        mOnMovieTrailerListener = onMovieTrailerListener;
    }

    public interface OnMovieTrailerListener{
        void onMovieTrailerClicked(String trailerKey);
    }

    @NonNull
    @Override
    public ReviewsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutIdItem = R.layout.review_item;
        View view = inflater.inflate(layoutIdItem, parent, false);
        return new ReviewsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsListViewHolder holder, int position) {
        Trailer trailer = mTrailers.get(position);
        holder.setTrailer(trailer);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class ReviewsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Trailer mTrailer;
        private TextView mTitle;

        ReviewsListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitle = itemView.findViewById(R.id.text_view_title);
        }

        void setTrailer(Trailer trailer) {
            mTitle.setText(trailer.getName());
            mTrailer = trailer;
        }

        @Override
        public void onClick(View v) {
            mOnMovieTrailerListener.onMovieTrailerClicked(mTrailer.getKey());
        }
    }
}
