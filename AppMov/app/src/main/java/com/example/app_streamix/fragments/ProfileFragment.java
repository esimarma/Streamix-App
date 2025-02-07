package com.example.app_streamix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_streamix.R;

public class ProfileFragment extends Fragment {

    private boolean isLoggedIn = true; // Default to logged-out state

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Logged-out layout
        View loggedOutLayout = view.findViewById(R.id.loggedOutLayout);
        View loggedInLayout = view.findViewById(R.id.loggedInLayout);

        // Profile components
        TextView userNameText = view.findViewById(R.id.userNameText);
        TextView displayEmail = view.findViewById(R.id.emailText);

        // Buttons
        Button loginButton = view.findViewById(R.id.loginButton);
        Button editProfileButton = view.findViewById(R.id.editUserButton);

        // Set localized button texts
        loginButton.setText(getString(R.string.login_button));
        editProfileButton.setText(getString(R.string.edit_profile));

        // Show the correct layout based on login state
        if (isLoggedIn) {
            showLoggedInLayout(loggedInLayout, loggedOutLayout);
        } else {
            showLoggedOutLayout(loggedInLayout, loggedOutLayout);
        }

        // Navigate to **LoginFragment** when clicking the login button
        loginButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new LoginFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Navigate to AccountFragment when clicking "Edit Profile"
        editProfileButton.setOnClickListener(v -> navigateToAccountFragment());

        return view;
    }

    private void navigateToAccountFragment() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new AccountFragment())
                .addToBackStack(null) // Ensures back navigation restores the profile screen
                .commit();
    }

    private void showLoggedInLayout(View loggedInLayout, View loggedOutLayout) {
        loggedOutLayout.setVisibility(View.GONE);
        loggedInLayout.setVisibility(View.VISIBLE);
    }

    private void showLoggedOutLayout(View loggedInLayout, View loggedOutLayout) {
        loggedOutLayout.setVisibility(View.VISIBLE);
        loggedInLayout.setVisibility(View.GONE);
    }
}
