package com.example.app_streamix.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_streamix.R;
import com.example.app_streamix.models.ApiResponse;
import com.example.app_streamix.models.ListMedia;
import com.example.app_streamix.models.Media;
import com.example.app_streamix.models.User;
import com.example.app_streamix.models.UserList;
import com.example.app_streamix.repositories.ListMediaRepository;
import com.example.app_streamix.repositories.MediaRepository;
import com.example.app_streamix.repositories.UserListRepository;
import com.example.app_streamix.utils.ApiConstants;
import com.example.app_streamix.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeriesDetailsFragment extends Fragment {

    private static final String ARG_SERIES_ID = "series_id";
    private int seriesId;
    private MediaRepository mediaRepository;
    private UserListRepository userListRepository;
    private ListMediaRepository listMediaRepository;
    private User user = new User();

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
        userListRepository = new UserListRepository();
        listMediaRepository = new ListMediaRepository();

        // Inicializa user
        SessionManager sessionManager = new SessionManager(getContext());
        if (sessionManager.isLoggedIn()) {
            user = sessionManager.getUser();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_series_details, container, false);

        // Initialize UI components
        seriesPoster = view.findViewById(R.id.seriesPoster);
       // backButton = view.findViewById(R.id.backButton);
        favoriteButton = view.findViewById(R.id.favoriteButton);
        seriesTitle = view.findViewById(R.id.seriesTitle);
        seriesMetadata = view.findViewById(R.id.seriesMetadata);
        seriesSynopsis = view.findViewById(R.id.seriesSynopsis);
        //seriesPlatforms = view.findViewById(R.id.seriesPlatforms);
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
       // backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }

    private void fetchSeriesDetails() {
        mediaRepository.getMediaById(seriesId, "tv").enqueue(new Callback<Media>() {
            @Override
            public void onResponse(Call<Media> call, Response<Media> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Media series = response.body();

                    seriesTitle.setText(series.getName());
                    seriesMetadata.setText(series.getFirstAirDate() + " • " + series.getVoteAverage() + "/10 • "
                            + series.getNumberOfSeasons() + " Temporadas • "
                            + series.getNumberOfEpisodes() + " Episódios");

                    seriesSynopsis.setText(series.getOverview());

                    // Load series poster
                    Glide.with(requireContext())
                            .load(ApiConstants.BASE_URL_IMAGE + series.getPosterPath())
                            .into(seriesPoster);

                    favoriteButton.setOnClickListener(v -> {
                        showAddDialog(getContext(), series);
                    });
                }
            }

            @Override
            public void onFailure(Call<Media> call, Throwable t) {
                Log.e("SeriesDetailsFragment", "Error fetching series details: " + t.getMessage());
            }
        });
    }

    private void showAddDialog(Context context, Media media) {
        String[] options = {
                context.getString(R.string.to_watch),
                context.getString(R.string.watched),
                context.getString(R.string.favorites)
        };

        boolean[] selectedItems = {false, false, false};

        LayoutInflater inflater = LayoutInflater.from(context);
        View titleView = inflater.inflate(R.layout.custom_dialog_title, null);
        TextView titleTextView = titleView.findViewById(R.id.customDialogTitle);
        titleTextView.setText(context.getString(R.string.choose_list));

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCustomTitle(titleView)
                .setMultiChoiceItems(options, selectedItems, (dialogInterface, which, isChecked) -> {
                    selectedItems[which] = isChecked;
                })
                .setPositiveButton(context.getString(R.string.confirm), (dialogInterface, which) -> {
                    List<String> selectedLists = new ArrayList<>();

                    if (selectedItems[0]) selectedLists.add("watchlist");
                    if (selectedItems[1]) selectedLists.add("visto");
                    if (selectedItems[2]) selectedLists.add("favorito");

                    for (String listType : selectedLists) {
                        getUserList(listType, media); // Passando a mídia como parâmetro
                    }
                })
                .setNegativeButton(context.getString(R.string.cancel), (dialogInterface, which) -> dialogInterface.dismiss())
                .create();

        dialog.setOnShowListener(d -> {
            ListView listView = dialog.getListView();
            if (listView != null) {
                for (int i = 0; i < listView.getChildCount(); i++) {
                    TextView item = (TextView) listView.getChildAt(i);
                    if (item != null) {
                        item.setTextColor(context.getColor(R.color.white));
                    }
                }
            }
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getColor(R.color.white));
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.app_background);
    }
    public void getUserList(String listType, Media media) {
        userListRepository.getByListType(listType, user.getId()).enqueue(new Callback<ApiResponse<UserList>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserList>> call, Response<ApiResponse<UserList>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    UserList userList = response.body().getData();
                    getListMedia(userList, media);
                } else {
                    Log.e("API_ERROR", "Erro ao carregar listas: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserList>> call, Throwable t) {
                Log.e("API_ERROR", "Falha na requisição: " + t.getMessage());
            }
        });
    }
    public void getListMedia(UserList userList, Media media) {
        listMediaRepository.getByUserListId(userList.getId()).enqueue(new Callback<ApiResponse<List<ListMedia>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ListMedia>>> call, Response<ApiResponse<List<ListMedia>>> response) {

                boolean isInList = false;
                if(response.body() != null){
                    List<ListMedia> mediaList = response.body().getData();
                    for (ListMedia listMedia : mediaList) {
                        if (listMedia.getIdMedia().intValue() == media.getId().intValue()) {
                            isInList = true;
                            break;
                        }
                    }
                }

                if (!isInList) {
                    createNewListMedia(userList, media);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ListMedia>>> call, Throwable t) {
                Log.e("API_ERROR", "Falha ao carregar mídias: " + t.getMessage());
            }
        });
    }

    private void createNewListMedia(UserList userList, Media media) {
        ListMedia listMedia = new ListMedia();
        listMedia.setIdMedia(media.getId());
        listMedia.setUserListId(userList.getId());
        listMedia.setMediaType(media.getName() != null ? "tv" : "movie");

        listMediaRepository.createListMedia(listMedia).enqueue(new Callback<ListMedia>() {
            @Override
            public void onResponse(Call<ListMedia> call, Response<ListMedia> response) {
                if (response.isSuccessful()) {
                    Log.d("API_SUCCESS", "Mídia adicionada com sucesso!");
                } else {
                    Log.e("API_ERROR", "Falha ao adicionar mídia: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ListMedia> call, Throwable t) {
                Log.e("API_ERROR", "Erro ao conectar ao servidor: " + t.getMessage());
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
