package com.example.app_streamix.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.app_streamix.adapters.ItemAdapter;
import com.example.app_streamix.MainActivity;
import com.example.app_streamix.R;
import com.example.app_streamix.models.MediaResponse;
import com.example.app_streamix.repositories.MediaRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    MediaRepository mediaRepository;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize MediaRepository
        mediaRepository = new MediaRepository();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerViews
        RecyclerView popularMoviesRecycler = view.findViewById(R.id.popularMoviesRecycler);
        RecyclerView popularSeriesRecycler = view.findViewById(R.id.popularSeriesRecycler);
        RecyclerView topRatedMoviesRecycler = view.findViewById(R.id.topRatedMoviesRecycler);
        RecyclerView topRatedSeriesRecycler = view.findViewById(R.id.topRatedSeriesRecycler);

        // Set horizontal layout managers
        popularMoviesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        popularSeriesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        topRatedMoviesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        topRatedSeriesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Set adapters

        mediaRepository.getPopularMovies().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<MediaResponse> call, Response<MediaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    popularMoviesRecycler.setAdapter(new ItemAdapter(response.body(), mediaRepository));
                }
            }

            @Override
            public void onFailure(Call<MediaResponse> call, Throwable t) {
                Log.d("HomeFragment", "onFailure: " + t.getMessage());
            }
        });

        mediaRepository.getPopularSeries().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<MediaResponse> call, Response<MediaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    popularSeriesRecycler.setAdapter(new ItemAdapter(response.body(), mediaRepository));
                }
            }

            @Override
            public void onFailure(Call<MediaResponse> call, Throwable t) {
                Log.d("HomeFragment", "onFailure: " + t.getMessage());
            }
        });

        mediaRepository.getTopRatedMovies().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<MediaResponse> call, Response<MediaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    topRatedMoviesRecycler.setAdapter(new ItemAdapter(response.body(), mediaRepository));
                }
            }

            @Override
            public void onFailure(Call<MediaResponse> call, Throwable t) {
                Log.d("HomeFragment", "onFailure: " + t.getMessage());
            }
        });

        mediaRepository.getTopRatedSeries().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<MediaResponse> call, Response<MediaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    topRatedSeriesRecycler.setAdapter(new ItemAdapter(response.body(), mediaRepository));
                }
            }

            @Override
            public void onFailure(Call<MediaResponse> call, Throwable t) {
                Log.d("HomeFragment", "onFailure: " + t.getMessage());
            }
        });

        // Set button click listener
        Button exploreMoreButton = view.findViewById(R.id.exploreMoreButton);
        exploreMoreButton.setOnClickListener(v -> {
            // Navigate to the Search page
            ((MainActivity) requireActivity()).navigateToSearch();
        });

        return view;
    }
}
