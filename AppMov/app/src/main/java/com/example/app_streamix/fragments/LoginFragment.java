package com.example.app_streamix.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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

import com.example.app_streamix.models.LoginRequest;
import com.example.app_streamix.models.LoginResponse;
import com.example.app_streamix.repositories.AuthRepository;
import com.example.app_streamix.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private AuthRepository authRepository;  // Repositório para lidar com a API
    private SessionManager sessionManager;

    public LoginFragment() {
        // Construtor vazio necessário
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Inicializar o repositório
        authRepository = new AuthRepository();

        // Inicializar Views
        Button switchToRegisterButton = view.findViewById(R.id.switchToRegisterButton);
        Button loginButton = view.findViewById(R.id.loginButton);
        ImageView backButton = view.findViewById(R.id.back_button);
        EditText emailField = view.findViewById(R.id.emailFieldLogin);
        EditText passwordField = view.findViewById(R.id.passwordFieldLogin);
        CheckBox showPasswordCheckbox = view.findViewById(R.id.show_password);

        // Inicializar o SessionManager
        sessionManager = new SessionManager(requireContext());

        // Mostrar ou ocultar a senha
        showPasswordCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        // Navegar para o RegisterFragment
        switchToRegisterButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Botão de voltar - vai para o ProfileFragment
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new ProfileFragment())
                    .commit();
        });

        // Lógica de login ao clicar no botão
        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                // Chama a função para fazer login
                loginUser(email, password);
            } else {
                Toast.makeText(getContext(), "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void loginUser(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);

        authRepository.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();

                    Long id = response.body().getUser().getId();
                    String userName = response.body().getUser().getName();
                    String userEmail = response.body().getUser().getEmail();
                    Integer movieWastedTime = response.body().getUser().getMovieWastedTimeMin();
                    Integer seriesWastedTime = response.body().getUser().getSeriesWastedTimeMin();

                    Toast.makeText(getContext(), "Login bem-sucedido!", Toast.LENGTH_SHORT).show();

                    // ✅ Armazena os dados do usuário na sessão
                    sessionManager.setLoggedIn(true);
                    sessionManager.setUserData(id, userName, userEmail, movieWastedTime, seriesWastedTime);

                    // Redireciona para o ProfileFragment
                    ProfileFragment profileFragment = new ProfileFragment();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, profileFragment)
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Credenciais inválidas. Tente novamente.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("Login", "onFailure: " + t.getMessage());
                Toast.makeText(getContext(), "Erro ao conectar com o servidor: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}