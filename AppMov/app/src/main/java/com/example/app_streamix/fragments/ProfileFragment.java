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
import com.example.app_streamix.utils.SessionManager;

public class ProfileFragment extends Fragment {

    private SessionManager sessionManager;

    public ProfileFragment() {
        // Construtor vazio necessário
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sessionManager = new SessionManager(requireContext()); // Inicializa o SessionManager

        View loggedOutLayout = view.findViewById(R.id.loggedOutLayout);
        View loggedInLayout = view.findViewById(R.id.loggedInLayout);

        TextView userNameText = view.findViewById(R.id.userNameText);

        Button loginButton = view.findViewById(R.id.loginButton);
        Button editProfileButton = view.findViewById(R.id.editUserButton);
       // Button logoutButton = view.findViewById(R.id.logoutButton); // Botão de Logout

        loginButton.setText(getString(R.string.login_button));
        editProfileButton.setText(getString(R.string.edit_profile));

        // Verifica o estado de login
        if (sessionManager.isLoggedIn()) {
            showLoggedInLayout(loggedInLayout, loggedOutLayout);

            String userName = sessionManager.getUserName();
            String userEmail = sessionManager.getUserEmail();

            userNameText.setText("Bem-vindo(a), " + userName + "!");
        } else {
            showLoggedOutLayout(loggedInLayout, loggedOutLayout);
        }

        // Navegar para o LoginFragment
        loginButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new LoginFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Navegar para o AccountFragment
        editProfileButton.setOnClickListener(v -> navigateToAccountFragment());

        // Lógica de Logout
       /* logoutButton.setOnClickListener(v -> {
            sessionManager.logout();
            showLoggedOutLayout(loggedInLayout, loggedOutLayout);
        });*/

        return view;
    }

    private void navigateToAccountFragment() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new AccountFragment())
                .addToBackStack(null)
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