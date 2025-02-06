package com.example.app_streamix.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_streamix.R;

public class LoginRegisterFragment extends Fragment {

    private LinearLayout loginLayout, registerLayout;
    private Button switchToRegisterButton, switchToLoginButton;

    public LoginRegisterFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_register, container, false);

        // Initialize Views
        loginLayout = view.findViewById(R.id.loginLayout);
        registerLayout = view.findViewById(R.id.registerLayout);
        switchToRegisterButton = view.findViewById(R.id.switchToRegisterButton);
        switchToLoginButton = view.findViewById(R.id.switchToLoginButton);

        // Settings Button
        ImageView settingsButton = view.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            // Navigate to SettingsFragment
            Fragment settingsFragment = new SettingsFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, settingsFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Switch to Register Form
        switchToRegisterButton.setOnClickListener(v -> {
            loginLayout.setVisibility(View.GONE);
            registerLayout.setVisibility(View.VISIBLE);
        });

        // Switch to Login Form
        switchToLoginButton.setOnClickListener(v -> {
            registerLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        });

        // Handle Login Button
        Button loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            EditText emailField = view.findViewById(R.id.emailFieldLogin);
            EditText passwordField = view.findViewById(R.id.passwordFieldLogin);

            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                // Mock Login Handling
                Toast.makeText(getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Register Button
        Button registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            EditText nameField = view.findViewById(R.id.nameFieldRegister);
            EditText emailField = view.findViewById(R.id.emailFieldRegister);
            EditText passwordField = view.findViewById(R.id.passwordFieldRegister);
            EditText confirmPasswordField = view.findViewById(R.id.confirmPasswordField);

            String name = nameField.getText().toString();
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            String confirmPassword = confirmPasswordField.getText().toString();

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && password.equals(confirmPassword)) {
                // Mock Registration Handling
                Toast.makeText(getContext(), "Account Created Successfully for " + name + "!", Toast.LENGTH_SHORT).show();
            } else {
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getContext(), "Please provide your name.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getContext(), "Please provide an email.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext(), "Please provide a password.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
