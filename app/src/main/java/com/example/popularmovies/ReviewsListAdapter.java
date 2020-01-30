package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmovies.retrofit.models.Review;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewsListAdapter extends RecyclerView.Adapter<ReviewsListAdapter.ReviewsListViewHolder> {

    private List<Review> mReviews;

    public ReviewsListAdapter(List<Review> reviews) {
        mReviews = reviews;
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
        Review review = mReviews.get(position);
        holder.setTitleAndContent(review.getAuthor(), review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ReviewsListViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mContent;

        public ReviewsListViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.text_view_title);
            mContent = itemView.findViewById(R.id.text_view_content);
        }

        public void setTitleAndContent(String title, String content) {
            mTitle.setText(title);
            mContent.setText(content);
        }
    }
}
