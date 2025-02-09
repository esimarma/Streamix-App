package com.example.app_streamix.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app_streamix.R;
import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    private final List<String> settingsOptions;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(String option);
    }

    public SettingsAdapter(List<String> settingsOptions, OnItemClickListener listener) {
        this.settingsOptions = settingsOptions;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String option = settingsOptions.get(position);
        holder.settingOptionText.setText(option);

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(option));
    }

    @Override
    public int getItemCount() {
        return settingsOptions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView settingOptionText;
        ImageView settingOptionIcon;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            settingOptionText = itemView.findViewById(R.id.setting_option_text);
            settingOptionIcon = itemView.findViewById(R.id.setting_option_icon);
        }
    }
}
