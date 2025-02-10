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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsFragment extends Fragment {

    private static final String ARG_MOVIE_ID = "movie_id";
    private int movieId;
    private MediaRepository mediaRepository;
    private UserListRepository userListRepository;
    private ListMediaRepository listMediaRepository;
    private User user = new User();

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
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        // Initialize UI components
        moviePoster = view.findViewById(R.id.moviePoster);
       // backButton = view.findViewById(R.id.backButton);
        favoriteButton = view.findViewById(R.id.favoriteButton);
        movieTitle = view.findViewById(R.id.movieTitle);
        movieMetadata = view.findViewById(R.id.movieMetadata);
        movieSynopsis = view.findViewById(R.id.movieSynopsis);
       // moviePlatforms = view.findViewById(R.id.moviePlatforms);

        // Fetch and display movie details
        fetchMovieDetails();

        // Back button functionality
        //backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

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

                    // Display Genres
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

                    favoriteButton.setOnClickListener(v -> {
                        showAddDialog(getContext(), movie);
                    });
                }
            }

            @Override
            public void onFailure(Call<Media> call, Throwable t) {
                Log.e("MovieDetailsFragment", "Error fetching movie details: " + t.getMessage());
            }
        });
    }

    private void showAddDialog(Context context, Media media) {
        String[] options = {
                context.getString(R.string.to_watch),
                context.getString(R.string.watched),
                context.getString(R.string.favorites)
        };

        List<String> availableLists = new ArrayList<>();
        List<String> listTypes = List.of("watchlist", "visto", "favorito");

        AtomicInteger pendingRequests = new AtomicInteger(listTypes.size());

        for (int i = 0; i < listTypes.size(); i++) {
            String listType = listTypes.get(i);
            String option = options[i];

            userListRepository.getByListType(listType, user.getId()).enqueue(new Callback<ApiResponse<UserList>>() {
                @Override
                public void onResponse(Call<ApiResponse<UserList>> call, Response<ApiResponse<UserList>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        UserList userList = response.body().getData();
                        listMediaRepository.getByUserListId(userList.getId()).enqueue(new Callback<ApiResponse<List<ListMedia>>>() {
                            @Override
                            public void onResponse(Call<ApiResponse<List<ListMedia>>> call, Response<ApiResponse<List<ListMedia>>> response) {
                                boolean isInList = false;
                                if (response.body() != null) {
                                    for (ListMedia listMedia : response.body().getData()) {
                                        if (listMedia.getIdMedia().intValue() == media.getId().intValue()) {
                                            isInList = true;
                                            break;
                                        }
                                    }
                                }
                                if (!isInList) {
                                    availableLists.add(option); // Adiciona somente se não estiver na lista
                                }
                                if (pendingRequests.decrementAndGet() == 0) {
                                    displayAddDialog(context, media, availableLists);
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse<List<ListMedia>>> call, Throwable t) {
                                if (pendingRequests.decrementAndGet() == 0) {
                                    displayAddDialog(context, media, availableLists);
                                }
                            }
                        });
                    } else {
                        if (pendingRequests.decrementAndGet() == 0) {
                            displayAddDialog(context, media, availableLists);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<UserList>> call, Throwable t) {
                    if (pendingRequests.decrementAndGet() == 0) {
                        displayAddDialog(context, media, availableLists);
                    }
                }
            });
        }
    }
    private void displayAddDialog(Context context, Media media, List<String> availableLists) {
        if (availableLists.isEmpty()) {
            Toast.makeText(context, "Esta mídia já está em todas as listas.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean[] selectedItems = new boolean[availableLists.size()];

        LayoutInflater inflater = LayoutInflater.from(context);
        View titleView = inflater.inflate(R.layout.custom_dialog_title, null);
        TextView titleTextView = titleView.findViewById(R.id.customDialogTitle);
        titleTextView.setText(context.getString(R.string.choose_list));
        titleTextView.setTextColor(context.getColor(R.color.white)); // Cor do título

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCustomTitle(titleView)
                .setMultiChoiceItems(availableLists.toArray(new String[0]), selectedItems, (dialogInterface, which, isChecked) -> {
                    selectedItems[which] = isChecked;
                })
                .setPositiveButton(context.getString(R.string.confirm), (dialogInterface, which) -> {
                    for (int i = 0; i < availableLists.size(); i++) {
                        if (selectedItems[i]) {
                            String selectedList = availableLists.get(i);
                            getUserList(selectedList, media);  // Busca a lista do usuário
                        }
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
                        item.setTextColor(context.getColor(R.color.white)); // Cor dos itens da lista
                    }
                }
            }
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getColor(R.color.white));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getColor(R.color.white));
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.app_background); // Cor de fundo
    }
    public void getUserList(String listType, Media media) {
        if(listType.equals("Para Ver")){
            listType = "watchlist";
        }
        userListRepository.getByListType(listType, user.getId()).enqueue(new Callback<ApiResponse<UserList>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserList>> call, Response<ApiResponse<UserList>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    UserList userList = response.body().getData();
                    //getListMedia(userList, media);
                    createNewListMedia(userList, media);
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

}
