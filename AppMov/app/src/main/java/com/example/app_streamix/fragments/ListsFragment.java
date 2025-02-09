package com.example.app_streamix.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_streamix.R;
import android.util.Log;
import android.widget.Toast;

import com.example.app_streamix.adapters.ItemAdapter;
import com.example.app_streamix.models.ApiResponse;
import com.example.app_streamix.models.Media;
import com.example.app_streamix.models.MediaResponse;
import com.example.app_streamix.models.User;
import com.example.app_streamix.models.UserList;
import com.example.app_streamix.repositories.MediaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.app_streamix.models.ListMedia;
import com.example.app_streamix.repositories.ListMediaRepository;
import com.example.app_streamix.repositories.UserListRepository;
import com.example.app_streamix.utils.SessionManager;

public class ListsFragment extends Fragment {

    private RecyclerView listRecycler;
    private ItemAdapter itemAdapter;
    private ListMediaRepository listMediaRepository;
    private MediaRepository mediaRepository;
    private UserListRepository userListRepository;
    private Button watchlistButton, favoriteButton, watchedButton;
    private User user = new User();
    MediaResponse mediaList;

    public ListsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);

        // Inicializa user
        SessionManager sessionManager = new SessionManager(requireContext());
        if (sessionManager.isLoggedIn()) {
            user = sessionManager.getUser();
        }

        // Inicializa repositórios
        listMediaRepository = new ListMediaRepository();
        mediaRepository = new MediaRepository();
        userListRepository = new UserListRepository();

        // Inicializa RecyclerView
        listRecycler = view.findViewById(R.id.listRecycler);
        listRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Inicializa botões
        watchlistButton = view.findViewById(R.id.watchlistButton);
        favoriteButton = view.findViewById(R.id.favoriteButton);
        watchedButton = view.findViewById(R.id.watchedButton);

        // Inicializa listas
        mediaList = new MediaResponse();
        mediaList.setResults(new ArrayList<>());

        // Carrega lista inicial (Watchlist)
        loadList("watchlist");

        // Listeners dos botões para alternar listas
        watchlistButton.setOnClickListener(v -> {
            clearRecyclerView();
            loadList("watchlist");
            updateButtonColors(watchlistButton);
        });

        favoriteButton.setOnClickListener(v -> {
            clearRecyclerView();
            loadList("favorito");
            updateButtonColors(favoriteButton);
        });

        watchedButton.setOnClickListener(v -> {
            clearRecyclerView();
            loadList("visto");
            updateButtonColors(watchedButton);
        });

        return view;
    }

    private void clearRecyclerView() {
        mediaList.getResults().clear();
        itemAdapter = new ItemAdapter(mediaList, mediaRepository); // Recria o adapter
        listRecycler.setAdapter(itemAdapter);
    }

    private void updateButtonColors(Button selectedButton) {
        int selectedColor = Color.parseColor("#8C64A8");
        int defaultColor = Color.parseColor("#2A2A55");

        watchlistButton.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        favoriteButton.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        watchedButton.setBackgroundTintList(ColorStateList.valueOf(defaultColor));

        selectedButton.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
    }

    private void loadList(String listType) {
        userListRepository.getByListType(listType, user.getId()).enqueue(new Callback<ApiResponse<UserList>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserList>> call, Response<ApiResponse<UserList>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Handle successful response
                    UserList userList = response.body().getData();

                    listMediaRepository.getByUserListId(userList.getId()).enqueue(new Callback<ApiResponse<List<ListMedia>>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<List<ListMedia>>> call, Response<ApiResponse<List<ListMedia>>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                // Handle successful response
                                List<ListMedia> mediaItems = response.body().getData();
                                int totalItems = mediaItems.size();
                                AtomicInteger completedRequests = new AtomicInteger(0); // Contador de requisições concluídas

                                for (ListMedia listMedia : mediaItems) {
                                    fetchMediaDetails(listMedia.getIdMedia(), listMedia.getMediaType(), totalItems, completedRequests);
                                }
                            } else {
                                Toast.makeText(getContext(), "Erro ao carregar listas", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResponse<List<ListMedia>>> call, Throwable t) {
                            Log.e("API_ERROR", "Falha ao carregar mídias: " + t.getMessage());
                            Toast.makeText(getContext(), "Erro ao carregar mídias", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Erro ao carregar listas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserList>> call, Throwable t) {
                Log.e("API_ERROR", "Falha na requisição: " + t.getMessage());
                Toast.makeText(getContext(), "Falha ao conectar ao servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMediaDetails(int mediaId, String mediaType, int totalItems, AtomicInteger completedRequests) {
        mediaRepository.getMediaById(mediaId, mediaType).enqueue(new Callback<Media>() {
            @Override
            public void onResponse(Call<Media> call, Response<Media> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mediaList.getResults().add(response.body()); // Adiciona à lista
                }
                checkIfAllRequestsCompleted(totalItems, completedRequests.incrementAndGet());
            }

            @Override
            public void onFailure(Call<Media> call, Throwable t) {
                checkIfAllRequestsCompleted(totalItems, completedRequests.incrementAndGet());
            }
        });
    }

    private void checkIfAllRequestsCompleted(int totalItems, int completedRequests) {
        if (completedRequests == totalItems) {
            if (mediaList.getResults() != null && !mediaList.getResults().isEmpty()) {
                itemAdapter = new ItemAdapter(mediaList, mediaRepository);
                listRecycler.setAdapter(itemAdapter);
            } else {
                listRecycler.setAdapter(new ItemAdapter(new MediaResponse(), mediaRepository)); // Garante que a lista fique vazia sem erro
                Log.e("ListsFragment", "Nenhuma mídia encontrada.");
            }
        }
    }

    private void updateRecyclerView(Media media) {
        if (itemAdapter == null) {
            itemAdapter = new ItemAdapter(new MediaResponse(), mediaRepository);
            listRecycler.setAdapter(itemAdapter);
        }
        itemAdapter.addItem(media);
    }
}
