package com.example.app_streamix.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.app_streamix.R;
import com.example.app_streamix.fragments.MovieDetailsFragment;
import com.example.app_streamix.fragments.SeriesDetailsFragment;
import com.example.app_streamix.models.ApiResponse;
import com.example.app_streamix.models.ListMedia;
import com.example.app_streamix.models.Media;
import com.example.app_streamix.models.MediaResponse;
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

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private final List<Media> mediaList = new ArrayList<>();
    private final MediaRepository mediaRepository;
    private final UserListRepository userListRepository;
    private final ListMediaRepository listMediaRepository;
    private User user = new User();
    Media media;
    boolean isListFragment;
    private long listId;

    public ItemAdapter(MediaResponse movieResponse, MediaRepository mediaRepository, boolean isListFragment, long listId) {
        this.mediaList.addAll(movieResponse.getResults());
        this.mediaRepository = mediaRepository;
        this.userListRepository = new UserListRepository();
        this.listMediaRepository = new ListMediaRepository();
        this.isListFragment = isListFragment;
        this.listId = listId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);

        // Inicializa user
        SessionManager sessionManager = new SessionManager(view.getContext());
        if (sessionManager.isLoggedIn()) {
            user = sessionManager.getUser();
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        media = mediaList.get(position);
        String mediaType = (media.getName() == null) ? "movie" : "tv";

        if(isListFragment){
            holder.listAddIcon.setImageResource(R.drawable.ic_delete);
        } else {
            holder.listAddIcon.setImageResource(R.drawable.lists_black);
        }

        // Chamada assíncrona para buscar detalhes da mídia
        mediaRepository.getMediaById(media.getId(), mediaType).enqueue(new Callback<Media>() {
            @Override
            public void onResponse(Call<Media> call, Response<Media> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Media mediaResponse = response.body();

                    // Define o título no TextView
                    holder.titleTextView.setText(
                            mediaResponse.getTitle() != null ? mediaResponse.getTitle() : mediaResponse.getName()
                    );

                    // Carrega a imagem com Glide
                    String imageUrl = ApiConstants.BASE_URL_IMAGE + mediaResponse.getPosterPath();
                    Glide.with(holder.itemView.getContext())
                            .load(imageUrl)
                            .into(holder.itemImage);

                    holder.listAddIcon.setOnClickListener(v -> {
                        if (isListFragment) {
                            showDeleteDialog(holder.itemView.getContext(), () -> {
                                // Lógica para remover item da lista
                                deleteListMedia(position);
                            });
                        } else {
                            // Se não for uma lista, abre o diálogo de adição
                            showAddDialog(holder.itemView.getContext(), mediaResponse);
                        }
                    });

                    // ADD CLICK LISTENER HERE
                    holder.itemView.setOnClickListener(v -> {
                        FragmentActivity activity = (FragmentActivity) holder.itemView.getContext();
                        int mediaId = mediaResponse.getId();  // Use o ID atualizado da resposta

                        if (mediaType.equals("movie")) {
                            MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.newInstance(mediaId);
                            activity.getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragmentContainer, movieDetailsFragment)
                                    .addToBackStack(null)
                                    .commit();
                        } else if (mediaType.equals("tv")) {
                            SeriesDetailsFragment seriesDetailsFragment = SeriesDetailsFragment.newInstance(mediaId);
                            activity.getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragmentContainer, seriesDetailsFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Media> call, Throwable t) {
                Log.d("TAG", "onFailure (getByIdMovie): " + t.getMessage());
            }
        });
    }


    private void showDeleteDialog(Context context, Runnable onConfirm) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View titleView = inflater.inflate(R.layout.custom_dialog_title, null);
        TextView titleTextView = titleView.findViewById(R.id.customDialogTitle);
        titleTextView.setText("Tem a certeza que quer remover desta lista?");

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCustomTitle(titleView)
                .setPositiveButton("Sim", (dialogInterface, which) -> onConfirm.run())
                .setNegativeButton("Não", (dialogInterface, which) -> dialogInterface.dismiss())
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getColor(R.color.white));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getColor(R.color.white));
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.app_background);
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

    private void deleteListMedia(int position) {
        listMediaRepository.deleteListMedia(listId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    removeItem(position);
                    // Toast.makeText(getContext(), "Item removido com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getContext(), "Erro ao remover item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //Toast.makeText(getContext(), "Erro de conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeItem(int position) {
        mediaList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mediaList.size()); // Atualiza a lista
    }


    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView itemImage;
        ImageView listAddIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.itemTextView);
            itemImage = itemView.findViewById(R.id.itemImage);
            listAddIcon = itemView.findViewById(R.id.listAddIcon);
        }
    }
}