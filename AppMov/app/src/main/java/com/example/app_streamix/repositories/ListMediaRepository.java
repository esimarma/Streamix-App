package com.example.app_streamix.repositories;

import com.example.app_streamix.interfaces.ListMediaApi;
import com.example.app_streamix.models.ListMedia;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListMediaRepository {
    private static final String BASE_URL = "http://127.0.0.1:8000/api/";
    private static ListMediaApi listMediaApi;

    // Inicialização do Retrofit
    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        listMediaApi = retrofit.create(ListMediaApi.class);
    }

    // Método para acessar a API
    public Call<List<ListMedia>> getListMedia() {
        return listMediaApi.getListMedia();
    }
}