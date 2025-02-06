package com.example.app_streamix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_streamix.R;

public class ProfileFragment extends Fragment {

    private boolean isLoggedIn = false; // Initial state for login mock
    private boolean isEditMode = false; // State for toggling edit mode

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Logged-out layout
        View loggedOutLayout = view.findViewById(R.id.loggedOutLayout);

        // Logged-in layout
        View loggedInLayout = view.findViewById(R.id.loggedInLayout);

        // Profile editing components
        LinearLayout profileDisplayLayout = view.findViewById(R.id.loggedInLayout);
        ScrollView profileEditLayout = view.findViewById(R.id.profileEditLayout);

        // Text and input fields
        TextView userNameText = view.findViewById(R.id.userNameText);
        TextView displayEmail = view.findViewById(R.id.emailText);
        EditText editNameField = view.findViewById(R.id.editNameField);
        EditText editEmailField = view.findViewById(R.id.editEmailField);
        EditText currentPasswordField = view.findViewById(R.id.currentPasswordField);
        EditText newPasswordField = view.findViewById(R.id.newPasswordField);
        EditText confirmPasswordField = view.findViewById(R.id.confirmPasswordField);

        // Buttons
        Button loginButton = view.findViewById(R.id.loginButton);
        Button editProfileButton = view.findViewById(R.id.editUserButton);
        Button saveChangesButton = view.findViewById(R.id.saveChangesButton);

        // Set localized button texts
        loginButton.setText(getString(R.string.login_button));
        editProfileButton.setText(getString(R.string.edit_profile));
        saveChangesButton.setText(getString(R.string.save_changes));

        // Simulate login state toggle
        if (isLoggedIn) {
            showLoggedInLayout(loggedInLayout, loggedOutLayout);
        } else {
            showLoggedOutLayout(loggedInLayout, loggedOutLayout);

            loginButton.setOnClickListener(v -> {
                // Navigate to LoginRegisterFragment
                Fragment loginRegisterFragment = new LoginRegisterFragment();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, loginRegisterFragment)
                        .addToBackStack(null)
                        .commit();
            });
        }

        // Edit Profile button
        editProfileButton.setOnClickListener(v -> {
            isEditMode = true;
            profileDisplayLayout.setVisibility(View.GONE);
            profileEditLayout.setVisibility(View.VISIBLE);

            // Pre-fill editable fields with current data
            editNameField.setText(userNameText.getText().toString());
            editEmailField.setText(displayEmail.getText().toString());
        });

        // Save Changes button
        saveChangesButton.setOnClickListener(v -> {
            String newName = editNameField.getText().toString();
            String newEmail = editEmailField.getText().toString();
            String currentPassword = currentPasswordField.getText().toString();
            String newPassword = newPasswordField.getText().toString();
            String confirmPassword = confirmPasswordField.getText().toString();

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(getContext(), getString(R.string.error_password_mismatch), Toast.LENGTH_SHORT).show();
                return;
            }

            // Mock password validation (replace with real validation later)
            if (!"mockpassword".equals(currentPassword)) {
                Toast.makeText(getContext(), getString(R.string.error_incorrect_password), Toast.LENGTH_SHORT).show();
                return;
            }

            // Save changes
            userNameText.setText(newName);
            displayEmail.setText(newEmail);

            // Exit edit mode
            isEditMode = false;
            profileEditLayout.setVisibility(View.GONE);
            profileDisplayLayout.setVisibility(View.VISIBLE);

            Toast.makeText(getContext(), getString(R.string.success_changes_saved), Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void showLoggedInLayout(View loggedInLayout, View loggedOutLayout) {
        loggedOutLayout.setVisibility(View.GONE);
        loggedInLayout.setVisibility(View.VISIBLE);

        // Example: Set user-specific details in the logged-in layout
        TextView userNameText = loggedInLayout.findViewById(R.id.userNameText);
        userNameText.setText(getString(R.string.mock_username)); // Mock username
    }

    private void showLoggedOutLayout(View loggedInLayout, View loggedOutLayout) {
        loggedOutLayout.setVisibility(View.VISIBLE);
        loggedInLayout.setVisibility(View.GONE);
    }
}
