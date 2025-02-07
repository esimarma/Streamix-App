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

public class RegisterFragment extends Fragment {

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Initialize Views
        Button switchToLoginButton = view.findViewById(R.id.switchToLoginButton);
        Button registerButton = view.findViewById(R.id.registerButton);
        ImageView backButton = view.findViewById(R.id.back_button); // Back Button
        EditText passwordField = view.findViewById(R.id.passwordFieldRegister);
        EditText confirmPasswordField = view.findViewById(R.id.confirmPasswordField);
        CheckBox showPasswordCheckbox = view.findViewById(R.id.show_password);

        // Show Password Toggle Logic
        showPasswordCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                confirmPasswordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                confirmPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        // Navigate back to LoginFragment
        switchToLoginButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new LoginFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Handle Back Button Click (Goes directly to ProfileFragment)
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new ProfileFragment()) // Always navigate back to Profile
                    .commit();
        });

        // Handle Register Button Click
        registerButton.setOnClickListener(v -> {
            EditText nameField = view.findViewById(R.id.nameFieldRegister);
            EditText emailField = view.findViewById(R.id.emailFieldRegister);

            String name = nameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && password.equals(confirmPassword)) {
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
