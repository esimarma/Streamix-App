package com.example.app_streamix.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_streamix.R;

public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize Views
        Button switchToRegisterButton = view.findViewById(R.id.switchToRegisterButton);
        Button loginButton = view.findViewById(R.id.loginButton);
        ImageView backButton = view.findViewById(R.id.back_button);
        EditText emailField = view.findViewById(R.id.emailFieldLogin);
        EditText passwordField = view.findViewById(R.id.passwordFieldLogin);
        CheckBox showPasswordCheckbox = view.findViewById(R.id.show_password); // Show Password Checkbox

        // Toggle password visibility
        showPasswordCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        // Navigate to RegisterFragment
        switchToRegisterButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Back Button - Goes to ProfileFragment
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new ProfileFragment())
                    .commit();
        });

        // Handle Login Button Click
        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
