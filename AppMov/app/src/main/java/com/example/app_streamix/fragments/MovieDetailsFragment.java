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

import com.bumptech.glide.Glide;
import com.example.app_streamix.R;

import com.example.app_streamix.models.Media;
import com.example.app_streamix.repositories.MediaRepository;
import com.example.app_streamix.utils.ApiConstants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsFragment extends Fragment {

    private static final String ARG_MOVIE_ID = "movie_id";
    private int movieId;
    private MediaRepository mediaRepository;

    private ImageView moviePoster, backButton, favoriteButton;
    private TextView movieTitle, movieMetadata, movieSynopsis, moviePlatforms;

    public static MovieDetailsFragment newInstance(int movieId) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieId = getArguments().getInt(ARG_MOVIE_ID);
        }
        mediaRepository = new MediaRepository();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        // Initialize UI components
        moviePoster = view.findViewById(R.id.moviePoster);
        backButton = view.findViewById(R.id.backButton);
        favoriteButton = view.findViewById(R.id.favoriteButton);
        movieTitle = view.findViewById(R.id.movieTitle);
        movieMetadata = view.findViewById(R.id.movieMetadata);
        movieSynopsis = view.findViewById(R.id.movieSynopsis);
        moviePlatforms = view.findViewById(R.id.moviePlatforms);

        // Fetch and display movie details
        fetchMovieDetails();

        // Back button functionality
        backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }

    private void fetchMovieDetails() {
        mediaRepository.getMediaById(movieId, "movie").enqueue(new Callback<Media>() {
            @Override
            public void onResponse(Call<Media> call, Response<Media> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Media movie = response.body();

                    movieTitle.setText(movie.getTitle());
                    movieMetadata.setText(movie.getReleaseDate() + " • " + movie.getVoteAverage() + "/10");

                    // ✅ Display Genres
                    if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
                        List<String> genreNames = new ArrayList<>();
                        for (Media.Genre genre : movie.getGenres()) {
                            genreNames.add(genre.getName());
                        }
                        movieMetadata.append(" • " + String.join(", ", genreNames)); // Append genres to metadata
                    } else {
                        movieMetadata.append(" • No genres available");
                    }

                    movieSynopsis.setText(movie.getOverview());

                    // Load movie poster
                    Glide.with(requireContext())
                            .load(ApiConstants.BASE_URL_IMAGE + movie.getPosterPath())
                            .into(moviePoster);
                }
            }

            @Override
            public void onFailure(Call<Media> call, Throwable t) {
                Log.e("MovieDetailsFragment", "Error fetching movie details: " + t.getMessage());
            }
        });
    }

}
