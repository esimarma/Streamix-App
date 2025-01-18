package com.example.app_streamix;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView searchRecyclerView;
    private SearchAdapter adapter;
    private List<String> itemList;

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
        ImageView searchButton = view.findViewById(R.id.searchButton);
        ImageView clearSearchButton = view.findViewById(R.id.clearSearchButton);
        RecyclerView searchRecyclerView = view.findViewById(R.id.searchRecyclerView);

        // Initialize data and adapter
        List<String> itemList = getMockData();
        SearchAdapter adapter = new SearchAdapter(itemList);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchRecyclerView.setAdapter(adapter);

        // Show/hide the clear button based on EditText content
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Show the clear button if there's text, hide otherwise
                if (s.length() > 0) {
                    clearSearchButton.setVisibility(View.VISIBLE);
                } else {
                    clearSearchButton.setVisibility(View.GONE);
                    adapter.updateData(itemList); // Reset RecyclerView if search is cleared
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Clear button functionality
        clearSearchButton.setOnClickListener(v -> {
            searchEditText.setText(""); // Clear the search bar
            clearSearchButton.setVisibility(View.GONE); // Hide the clear button
        });

        // Search functionality
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString();
            if (!TextUtils.isEmpty(query)) {
                filterResults(query, adapter, itemList);
            }
        });

        return view;
    }

    private void filterResults(String query, SearchAdapter adapter, List<String> itemList) {
        List<String> filteredList = new ArrayList<>();
        for (String item : itemList) {
            if (item.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.updateData(filteredList);
    }

    private List<String> getMockData() {
        List<String> data = new ArrayList<>();
        data.add("Filme com o nome #1 - Ano: 2023 - Gênero: Ação - Duração: 2h");
        data.add("Série com o nome #1 - Ano: 2021 - Gênero: Drama - Temporadas: 3");
        data.add("Série com o nome #2 - Ano: 2022 - Gênero: Comédia - Temporadas: 2");
        return data;
    }
}
