package com.example.app_streamix.repositories;


import com.example.app_streamix.interfaces.UserApi;
import com.example.app_streamix.models.User;
import com.example.app_streamix.utils.ApiConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRepository {
    private static String baseUrl = ApiConstants.BASE_URL;
    private static UserApi userApi;

    // Inicialização do Retrofit
    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userApi = retrofit.create(UserApi.class);
    }

    // Métodos para acessar a API
    public Call<List<User>> getUser() {
        return userApi.getUser();
    }

    public Call<User> getUserById(Long id) {
        return userApi.getUserById(id);
    }
}
