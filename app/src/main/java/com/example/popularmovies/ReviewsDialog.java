package com.example.popularmovies;


import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.popularmovies.retrofit.models.Review;

import java.io.Serializable;
import java.util.List;

public class ReviewsDialog extends DialogFragment {

    private static final String REVIEWS_PARAM = "REVIEWS_PARAM";

    private List<Review> mReviews;
    private ReviewsListAdapter mAdapter;

    public ReviewsDialog() {
        // Required empty public constructor
    }

    public static ReviewsDialog newInstance(List<Review> reviews) {
        ReviewsDialog fragment = new ReviewsDialog();
        Bundle args = new Bundle();
        args.putSerializable(REVIEWS_PARAM, (Serializable) reviews);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reviews_dialog, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(REVIEWS_PARAM)) {
                mReviews = (List<Review>) arguments.getSerializable(REVIEWS_PARAM);
            }
        }

        populateUI(view);
        return view;
    }

    private void populateUI(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_reviews);

        mAdapter = new ReviewsListAdapter(mReviews);

        recyclerView.setAdapter(mAdapter);
    }
}
