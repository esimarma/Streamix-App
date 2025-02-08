package com.example.app_streamix.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.app_streamix.R;
import com.example.app_streamix.models.Media;
import com.example.app_streamix.models.MediaResponse;
import com.example.app_streamix.repositories.MediaRepository;
import com.example.app_streamix.utils.ApiConstants;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private final List<Media> mediaList = new ArrayList<>();
    private final MediaRepository mediaRepository;

    public ItemAdapter(MediaResponse movieResponse, MediaRepository mediaRepository) {
        this.mediaList.addAll(movieResponse.getResults());
        this.mediaRepository = mediaRepository;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Media media = mediaList.get(position); // Obtém o objeto Media
        int movieId = media.getId(); // Obtém o ID do filme

        // Faz a chamada assíncrona para obter o Media pelo ID do filme
        mediaRepository.getByIdMovie(movieId).enqueue(new Callback<Media>() {
            @Override
            public void onResponse(Call<Media> call, Response<Media> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Media mediaResponse = response.body();

                    // Define o título ou nome caso o título seja nulo
                    holder.titleTextView.setText(mediaResponse.getTitle());

                    // Busca a imagem do filme na API
                    String imageUrl = ApiConstants.BASE_URL_IMAGE + mediaResponse.getPosterPath();

                    // Usa Glide para carregar a imagem no ImageView
                    Glide.with(holder.itemView.getContext())
                            .load(imageUrl)
                            .into(holder.itemImage);
                }
            }

            @Override
            public void onFailure(Call<Media> call, Throwable t) {
                Log.d("TAG", "onFailure (getByIdMovie): " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView itemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.itemTextView);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }
}