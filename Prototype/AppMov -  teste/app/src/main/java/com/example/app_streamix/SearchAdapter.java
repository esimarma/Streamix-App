package com.example.app_streamix;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_streamix.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<String> data;

    public SearchAdapter(List<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = data.get(position);

        // For simplicity, we'll display the entire string in the title TextView
        holder.itemTitle.setText(item);

        // Mock some details (e.g., for demonstration purposes)
        holder.itemDetails.setText("Ano - Gênero - Duração");

        // Mock some description
        holder.itemDescription.setText("Sinopse do filme ou série");

        // Optionally, set an image (mocked or fetched from a URL)
        holder.itemImage.setImageResource(R.drawable.book_cover_trigun); // Replace with an actual drawable or URL loading logic
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(List<String> newData) {
        this.data = newData;
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
