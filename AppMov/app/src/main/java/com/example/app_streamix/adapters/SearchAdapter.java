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
import com.example.app_streamix.repositories.MediaRepository;
import com.example.app_streamix.utils.ApiConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Media> mediaList;
    private final MediaRepository mediaRepository;

    public SearchAdapter(List<Media> mediaList, MediaRepository mediaRepository) {
        this.mediaList = mediaList;
        this.mediaRepository = mediaRepository;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Media media = mediaList.get(position);
        String mediaType = "";
        if(media.getName() == null) {
            mediaType = "movie";
        } else {
            mediaType = "tv";
        }

        mediaRepository.getMediaById(media.getId(), mediaType).enqueue(new Callback<Media>() {
            @Override
            public void onResponse(Call<Media> call, Response<Media> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Media mediaResponse = response.body();

                    // Define o título ou nome caso o título seja nulo
                    if (mediaResponse.getTitle() == null) {
                        holder.itemTitle.setText(mediaResponse.getName());
                    } else {
                        holder.itemTitle.setText(mediaResponse.getTitle());
                    }
                    // Busca a imagem do filme na API
                    String imageUrl = ApiConstants.BASE_URL_IMAGE + mediaResponse.getPosterPath();

                    // Usa Glide para carregar a imagem no ImageView
                    Glide.with(holder.itemView.getContext())
                            .load(imageUrl)
                            .into(holder.itemImage);

                    holder.itemDetails.setText("Ano - Gênero - Duração");

                    holder.itemDescription.setText(mediaResponse.getOverview());
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

    public void updateData(List<Media> newData) {
        this.mediaList = newData;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTitle;
        TextView itemDetails;
        TextView itemDescription;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemDetails = itemView.findViewById(R.id.itemDetails);
            itemDescription = itemView.findViewById(R.id.itemDescription);
        }
    }
}
