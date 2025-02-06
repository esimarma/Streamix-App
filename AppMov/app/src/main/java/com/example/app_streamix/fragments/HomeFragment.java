package com.example.app_streamix.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.app_streamix.adapters.ItemAdapter;
import com.example.app_streamix.MainActivity;
import com.example.app_streamix.R;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

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
        popularMoviesRecycler.setAdapter(new ItemAdapter(getMockMovies()));
        popularSeriesRecycler.setAdapter(new ItemAdapter(getMockSeries()));
        topRatedMoviesRecycler.setAdapter(new ItemAdapter(getMockTopRatedMovies()));
        topRatedSeriesRecycler.setAdapter(new ItemAdapter(getMockTopRatedSeries()));

        // Set button click listener
        Button exploreMoreButton = view.findViewById(R.id.exploreMoreButton);
        exploreMoreButton.setOnClickListener(v -> {
            // Navigate to the Search page
            ((MainActivity) requireActivity()).navigateToSearch();
        });

        return view;
    }

    // Mock data for Top Rated Movies and Series
    private List<String> getMockTopRatedMovies() {
        return Arrays.asList("Top Filme #1", "Top Filme #2", "Top Filme #3", "Top Filme #4");
    }

    private List<String> getMockTopRatedSeries() {
        return Arrays.asList("Top Série #1", "Top Série #2", "Top Série #3");
    }

    private List<String> getMockMovies() {
        return Arrays.asList("ex filme #1", "ex filme #2", "ex filme #3", "ex filme #4");
    }

    private List<String> getMockSeries() {
        return Arrays.asList("ex serie #1", "ex serie #2", "ex serie #3");
    }

}
