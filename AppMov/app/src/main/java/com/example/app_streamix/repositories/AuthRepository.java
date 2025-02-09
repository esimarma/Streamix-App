package com.example.app_streamix.repositories;

import com.example.app_streamix.interfaces.AuthApi;
import com.example.app_streamix.models.LoginRequest;
import com.example.app_streamix.models.LoginResponse;
import com.example.app_streamix.models.RegisterRequest;
import com.example.app_streamix.models.User;
import com.example.app_streamix.utils.ApiConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Call;

public class AuthRepository {
    private static final String BASE_URL = ApiConstants.BASE_URL;
    private static AuthApi authApi;

    // Inicialização do Retrofit
    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authApi = retrofit.create(AuthApi.class);
    }

    public Call<LoginResponse> login(LoginRequest loginRequest) {
        return authApi.login(loginRequest);
    }

    public Call<Void> logout(String token) {
        return authApi.logout("Bearer " + token);
    }

    public Call<User> getUser(String token) {
        return authApi.getUser("Bearer " + token);
    }

    public Call<Void> register(RegisterRequest registerRequest) {
        return authApi.register(registerRequest);
    }
}
