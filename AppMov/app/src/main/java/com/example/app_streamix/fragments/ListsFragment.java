package com.example.app_streamix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_streamix.R;

public class ListsFragment extends Fragment {

    private LinearLayout personalizedLayout;
    private ScrollView watchlistLayout;

    public ListsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);

        // Initialize layouts
        watchlistLayout = view.findViewById(R.id.watchlistLayout);
        personalizedLayout = view.findViewById(R.id.personalizedLayout);

        Button watchlistButton = view.findViewById(R.id.watchlistButton);
        Button personalizedButton = view.findViewById(R.id.personalizedButton);

        // Set initial state
        showWatchlist();

        // Switch between layouts
        watchlistButton.setOnClickListener(v -> showWatchlist());
        personalizedButton.setOnClickListener(v -> showPersonalized());

        return view;
    }

    private void showWatchlist() {
        watchlistLayout.setVisibility(View.VISIBLE);
        personalizedLayout.setVisibility(View.GONE);
    }

    private void showPersonalized() {
        watchlistLayout.setVisibility(View.GONE);
        personalizedLayout.setVisibility(View.VISIBLE);
    }
}
