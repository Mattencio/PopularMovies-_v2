package com.example.popularmovies.views;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.popularmovies.R;
import com.example.popularmovies.TrailersListAdapter;
import com.example.popularmovies.retrofit.models.Trailer;

import java.io.Serializable;
import java.util.List;

public class TrailersDialogFragment extends DialogFragment implements TrailersListAdapter.OnMovieTrailerListener {

    private static final String TRAILERS_PARAM = "TRAILERS_PARAM";
    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private List<Trailer> mTrailers;

    public TrailersDialogFragment() {
        // Required empty public constructor
    }

    public static TrailersDialogFragment newInstance(List<Trailer> trailers) {
        TrailersDialogFragment fragment = new TrailersDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(TRAILERS_PARAM, (Serializable) trailers);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trailers_dialog, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(TRAILERS_PARAM)) {
                mTrailers = (List<Trailer>) arguments.getSerializable(TRAILERS_PARAM);
            }
        }

        populateUI(view);

        return view;
    }

    private void populateUI(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_trailers);

        TrailersListAdapter adapter = new TrailersListAdapter(mTrailers, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onMovieTrailerClicked(String trailerKey) {
        Intent videoIntent = new Intent(Intent.ACTION_VIEW);
        Uri videoUrl = Uri.parse(YOUTUBE_URL + trailerKey);
        videoIntent.setData(videoUrl);
        startActivity(videoIntent);
    }
}
