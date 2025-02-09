package com.example.app_streamix.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_streamix.R;
import com.example.app_streamix.models.ApiResponse;
import com.example.app_streamix.models.User;
import com.example.app_streamix.repositories.UserRepository;
import com.example.app_streamix.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        TextView moviesHoursText = view.findViewById(R.id.moviesHoursText);

        Button loginButton = view.findViewById(R.id.loginButton);
        Button editProfileButton = view.findViewById(R.id.editUserButton);

        UserRepository userRepository = new UserRepository();

        loginButton.setText(getString(R.string.login_button));
        editProfileButton.setText(getString(R.string.edit_profile));

        if (sessionManager.isLoggedIn()) {
            showLoggedInLayout(loggedInLayout, loggedOutLayout);

            User user = sessionManager.getUser();
            if (user != null) {
                userRepository.getUserById(user.getId()).enqueue(new Callback<ApiResponse <User>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                        if (response.isSuccessful()) {
                            User userResponse = response.body().getData();
                            if (userResponse != null) {
                                userNameText.setText("Bem-vindo(a), " + userResponse.getName() + "!");
                                moviesHoursText.setText(hoursToMinutes(userResponse.getMovieWastedTimeMin()) + "h de filmes");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                        Log.d("ProfileFragment", "Erro ao buscar usuário: " + t.getMessage());
                    }

                });
                } else {
                    userNameText.setText("Bem-vindo(a)!");
                }
        } else {
            showLoggedOutLayout(loggedInLayout, loggedOutLayout);
        }

        // Botão de login leva ao LoginFragment
        loginButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new LoginFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Botão de editar perfil leva ao AccountFragment
        editProfileButton.setOnClickListener(v -> navigateToAccountFragment());



        return view;
    }

    private double hoursToMinutes(int min) {
        return Double.parseDouble(String.format("%.2f", min / 60.0));
    }

    private void navigateToAccountFragment() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new AccountFragment())
                .addToBackStack(null)
                .commit();
    }

    public void onResume() {
        super.onResume();

        // Ensure the header is visible when coming back to Profile
        View topHeader = requireActivity().findViewById(R.id.top_header);
        if (topHeader != null) {
            topHeader.setVisibility(View.VISIBLE);
        }
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