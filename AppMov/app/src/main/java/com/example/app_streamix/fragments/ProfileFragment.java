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
import com.example.app_streamix.models.ListMedia;
import com.example.app_streamix.models.Media;
import com.example.app_streamix.models.User;
import com.example.app_streamix.models.UserList;
import com.example.app_streamix.repositories.ListMediaRepository;
import com.example.app_streamix.repositories.UserListRepository;
import com.example.app_streamix.repositories.UserRepository;
import com.example.app_streamix.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private SessionManager sessionManager;
    private UserListRepository userListRepository;
    private ListMediaRepository listMediaRepository;
    private User user;
    private int tvCount = 0;
    private int movieCount = 0;

    TextView countSeries;
    TextView countMovies;

    public ProfileFragment() {
        // Construtor vazio necessário
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sessionManager = new SessionManager(requireContext()); // Inicializa o SessionManager
        userListRepository = new UserListRepository();
        listMediaRepository = new ListMediaRepository();

        View loggedOutLayout = view.findViewById(R.id.loggedOutLayout);
        View loggedInLayout = view.findViewById(R.id.loggedInLayout);

        TextView userNameText = view.findViewById(R.id.userNameText);
        TextView moviesHoursText = view.findViewById(R.id.moviesHoursText);
        countSeries = view.findViewById(R.id.countSeries);
        countMovies = view.findViewById(R.id.countMovies);

        Button loginButton = view.findViewById(R.id.loginButton);
        Button editProfileButton = view.findViewById(R.id.editUserButton);

        UserRepository userRepository = new UserRepository();

        loginButton.setText(getString(R.string.login_button));
        editProfileButton.setText(getString(R.string.edit_profile));

        if (sessionManager.isLoggedIn()) {
            showLoggedInLayout(loggedInLayout, loggedOutLayout);

            user = sessionManager.getUser();
            if (user != null) {
                userRepository.getUserById(user.getId()).enqueue(new Callback<ApiResponse <User>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                        if (response.isSuccessful()) {
                            User userResponse = response.body().getData();
                            if (userResponse != null) {
                                getTvAndMovieCount();
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

    public void getTvAndMovieCount() {
        userListRepository.getByListType("visto", user.getId()).enqueue(new Callback<ApiResponse<UserList>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserList>> call, Response<ApiResponse<UserList>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    UserList userList = response.body().getData();

                    listMediaRepository.getByUserListId(userList.getId()).enqueue(new Callback<ApiResponse<List<ListMedia>>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<List<ListMedia>>> call, Response<ApiResponse<List<ListMedia>>> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                for (ListMedia listMedia : response.body().getData()) {
                                    if(listMedia.getMediaType().equals("tv")){
                                        tvCount ++;
                                    }
                                    else {
                                        movieCount++;
                                    }
                                    if((tvCount + movieCount) == response.body().getData().size()){
                                        countSeries.setText(tvCount + " Series vistas");
                                        countMovies.setText(movieCount + " Filmes vistos");
                                    }
                                }

                            } else {
                                Log.e("API_ERROR", "Erro ao carregar listas: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<List<ListMedia>>> call, Throwable t) {
                            Log.e("API_ERROR", "Falha na requisição: " + t.getMessage());
                        }

                    });
                } else {
                    Log.e("API_ERROR", "Erro ao carregar listas: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserList>> call, Throwable t) {
                Log.e("API_ERROR", "Falha na requisição: " + t.getMessage());
            }
        });
    }

    private double hoursToMinutes(int min) {
        return Math.round((min / 60.0) * 100.0) / 100.0; // Arredonda para 2 casas decimais
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