package com.example.app_streamix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_streamix.MainActivity;
import com.example.app_streamix.R;

public class ChangePasswordFragment extends Fragment {

    private EditText currentPassword, newPassword, confirmPassword;
    private Button confirmButton;
    private ImageView backButton;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        // Get reference to MainActivity to control the header visibility
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.hideHeader(); // Ensure header stays hidden

        // Initialize UI elements
        backButton = view.findViewById(R.id.back_button);
        currentPassword = view.findViewById(R.id.current_password);
        newPassword = view.findViewById(R.id.new_password);
        confirmPassword = view.findViewById(R.id.confirm_password);
        confirmButton = view.findViewById(R.id.confirm_button);

        // Handle back button navigation
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Confirm button click logic
        confirmButton.setOnClickListener(v -> changePassword());

        return view;
    }

    private void changePassword() {
        String currentPass = currentPassword.getText().toString().trim();
        String newPass = newPassword.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();

        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(getContext(), getString(R.string.error_password_mismatch), Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulating password validation
        if (!currentPass.equals("mockpassword")) {
            Toast.makeText(getContext(), getString(R.string.error_incorrect_password), Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulating password change success
        Toast.makeText(getContext(), getString(R.string.password_updated), Toast.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
