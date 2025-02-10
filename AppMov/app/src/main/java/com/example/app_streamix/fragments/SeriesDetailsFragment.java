package com.example.app_streamix.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_streamix.R;
import com.example.app_streamix.models.Media;
import com.example.app_streamix.repositories.MediaRepository;
import com.example.app_streamix.utils.ApiConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeriesDetailsFragment extends Fragment {

    private static final String ARG_SERIES_ID = "series_id";
    private int seriesId;
    private MediaRepository mediaRepository;

    private ImageView seriesPoster, backButton, favoriteButton;
    private TextView seriesTitle, seriesMetadata, seriesSynopsis, seriesPlatforms;
    private TextView tabSobre, tabEpisodios;
    private View sobreLayout;
    private RecyclerView episodesRecyclerView; // Ensure this is a RecyclerView, not a View

    public static SeriesDetailsFragment newInstance(int seriesId) {
        SeriesDetailsFragment fragment = new SeriesDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SERIES_ID, seriesId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            seriesId = getArguments().getInt(ARG_SERIES_ID);
        }
        mediaRepository = new MediaRepository();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_series_details, container, false);

        // Initialize UI components
        seriesPoster = view.findViewById(R.id.seriesPoster);
        backButton = view.findViewById(R.id.backButton);
        favoriteButton = view.findViewById(R.id.favoriteButton);
        seriesTitle = view.findViewById(R.id.seriesTitle);
        seriesMetadata = view.findViewById(R.id.seriesMetadata);
        seriesSynopsis = view.findViewById(R.id.seriesSynopsis);
        seriesPlatforms = view.findViewById(R.id.seriesPlatforms);
        tabSobre = view.findViewById(R.id.tabSobre);
        tabEpisodios = view.findViewById(R.id.tabEpisodios);
        sobreLayout = view.findViewById(R.id.sobreLayout);
        episodesRecyclerView = view.findViewById(R.id.episodesRecyclerView);
        episodesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        episodesRecyclerView.setVisibility(View.GONE); // Episodes are not shown for now

        // Fetch and display series details
        fetchSeriesDetails();

        // Tab switching logic
        setupTabSwitching();

        // Back button functionality
        backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }

    private void fetchSeriesDetails() {
        mediaRepository.getMediaById(seriesId, "tv").enqueue(new Callback<Media>() {
            @Override
            public void onResponse(Call<Media> call, Response<Media> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Media series = response.body();

                    seriesTitle.setText(series.getName());
                    seriesMetadata.setText(series.getReleaseDate() + " • " + series.getVoteAverage() + "/10 • "
                            + series.getNumberOfSeasons() + " Temporadas • "
                            + series.getNumberOfEpisodes() + " Episódios");

                    seriesSynopsis.setText(series.getOverview());

                    // Load series poster
                    Glide.with(requireContext())
                            .load(ApiConstants.BASE_URL_IMAGE + series.getPosterPath())
                            .into(seriesPoster);
                }
            }

            @Override
            public void onFailure(Call<Media> call, Throwable t) {
                Log.e("SeriesDetailsFragment", "Error fetching series details: " + t.getMessage());
            }
        });
    }

    private void setupTabSwitching() {
        tabSobre.setOnClickListener(v -> {
            tabSobre.setBackgroundResource(R.color.app_background);
            tabEpisodios.setBackgroundResource(R.color.app_background);
            sobreLayout.setVisibility(View.VISIBLE);
            episodesRecyclerView.setVisibility(View.GONE);
        });

        tabEpisodios.setOnClickListener(v -> {
            tabEpisodios.setBackgroundResource(R.color.app_background);
            tabSobre.setBackgroundResource(R.color.app_background);
            sobreLayout.setVisibility(View.GONE);
            episodesRecyclerView.setVisibility(View.VISIBLE);
        });
    }
}
