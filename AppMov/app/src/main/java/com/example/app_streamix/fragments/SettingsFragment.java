package com.example.app_streamix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_streamix.R;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Back Button
        view.findViewById(R.id.backButton).setOnClickListener(v -> {
            // Navigate back or close fragment
            requireActivity().onBackPressed();
        });

        // Account Option
        view.findViewById(R.id.accountOption).setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new AccountFragment()) // Navigate to AccountFragment
                    .addToBackStack(null) // Allow back navigation
                    .commit();
        });

        // Appearance Option
        view.findViewById(R.id.appearanceOption).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Aparência clicked", Toast.LENGTH_SHORT).show();
        });

        // Notifications Option
        view.findViewById(R.id.notificationsOption).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Notificações clicked", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
