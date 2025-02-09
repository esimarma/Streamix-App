package com.example.app_streamix.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_streamix.R;
import com.example.app_streamix.adapters.ItemAdapter;
import com.example.app_streamix.adapters.SearchAdapter;
import com.example.app_streamix.models.Media;
import com.example.app_streamix.models.MediaResponse;
import com.example.app_streamix.repositories.MediaRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    MediaRepository mediaRepository;

    private Handler searchHandler = new Handler();
    private Runnable searchRunnable;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView searchRecyclerView;
    private SearchAdapter adapter;
    private List<String> itemList;
    List<Media> mediaList;
    String mediaType;
    String query = "";

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaRepository = new MediaRepository();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize UI components
        EditText searchEditText = view.findViewById(R.id.searchEditText);
        ImageView clearSearchButton = view.findViewById(R.id.clearSearchButton);
        RecyclerView searchRecyclerView = view.findViewById(R.id.searchRecyclerView);
        ImageView filterButton = view.findViewById(R.id.filterButton);
        Button moviesButton = view.findViewById(R.id.moviesButton);
        Button tvButton = view.findViewById(R.id.tvButton);

        mediaType = "movie";
        mediaList = new ArrayList<>();
        searchMedia(query);

        adapter = new SearchAdapter(mediaList, mediaRepository);

        filterButton.setOnClickListener(v -> {
            FilterFragment filterFragment = new FilterFragment();
            filterFragment.show(getParentFragmentManager(), "FilterFragment");
        });

        moviesButton.setOnClickListener(v -> {
            mediaType = "movie";
            moviesButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8C64A8")));
            tvButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2A2A55")));
            searchMedia(query);
        });

        tvButton.setOnClickListener(v -> {
            mediaType = "tv";
            moviesButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2A2A55")));
            tvButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8C64A8")));
            searchMedia(query);
        });

        // Initialize data and adapter
        //List<String> itemList = getMockData();
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchRecyclerView.setAdapter(adapter);


        // Show/hide the clear button based on EditText content
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Mostra o botão de limpar se houver texto
                if (s.length() > 0) {
                    clearSearchButton.setVisibility(View.VISIBLE);
                } else {
                    clearSearchButton.setVisibility(View.GONE);
                    adapter.updateData(mediaList); // Reseta RecyclerView se a busca for apagada
                }

                // Cancela a pesquisa anterior se o usuário continuar digitando rapidamente
                searchHandler.removeCallbacks(searchRunnable);
            }

            @Override
            public void afterTextChanged(Editable s) {
                query = s.toString().trim();
                searchRunnable = () -> searchMedia(query);
                searchHandler.postDelayed(searchRunnable, 500); // Aguarda 500ms antes de pesquisar
            }
        });

        // Clear button functionality
        clearSearchButton.setOnClickListener(v -> {
            searchEditText.setText(""); // Clear the search bar
            clearSearchButton.setVisibility(View.GONE); // Hide the clear button
        });

        return view;
    }

    private void searchMedia(String query) {
        if(query == null || query.isEmpty()) {
            mediaList = new ArrayList<>();
            if (mediaType.equals("movie")) {
                mediaRepository.getPopularMovies().enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<MediaResponse> call, Response<MediaResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            mediaList = response.body().getResults();
                            adapter.updateData(mediaList);
                        }
                    }

                    @Override
                    public void onFailure(Call<MediaResponse> call, Throwable t) {
                        Log.d("HomeFragment", "onFailure: " + t.getMessage());
                    }
                });
            } else if (mediaType.equals("tv")) {
                mediaRepository.getPopularSeries().enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<MediaResponse> call, Response<MediaResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            mediaList = response.body().getResults();
                            adapter.updateData(mediaList);
                        }
                    }

                    @Override
                    public void onFailure(Call<MediaResponse> call, Throwable t) {
                        Log.d("HomeFragment", "onFailure: " + t.getMessage());
                    }
                });
            }
        }
        else {
            mediaRepository.searchMedia(query, mediaType).enqueue(new Callback<MediaResponse>() {
                @Override
                public void onResponse(Call<MediaResponse> call, Response<MediaResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        mediaList = response.body().getResults();
                        adapter.updateData(mediaList);
                    } else {
                        System.out.println("Erro na resposta: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<MediaResponse> call, Throwable t) {
                    System.out.println("Falha na requisição: " + t.getMessage());
                }
            });
        }
    }

    private void filterResults(String query, SearchAdapter adapter, List<String> itemList) {
        List<String> filteredList = new ArrayList<>();
        for (String item : itemList) {
            if (item.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        //adapter.updateData(filteredList);
    }

    private List<String> getMockData() {
        List<String> data = new ArrayList<>();
        data.add("Filme com o nome #1 - Ano: 2023 - Gênero: Ação - Duração: 2h");
        data.add("Série com o nome #1 - Ano: 2021 - Gênero: Drama - Temporadas: 3");
        data.add("Série com o nome #2 - Ano: 2022 - Gênero: Comédia - Temporadas: 2");
        return data;
    }
}
