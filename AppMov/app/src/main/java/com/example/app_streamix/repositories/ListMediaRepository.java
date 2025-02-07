package com.example.app_streamix.repositories;

import com.example.app_streamix.interfaces.ListMediaApi;
import com.example.app_streamix.models.ListMedia;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListMediaRepository {
    private static final String BASE_URL = "http://10.0.2.2:8000/api/";
    private static ListMediaApi listMediaApi;

    // Inicialização do Retrofit
    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        listMediaApi = retrofit.create(ListMediaApi.class);
    }

    // Métodos para acessar a API
    public Call<List<ListMedia>> getListMedia() {
        return listMediaApi.getListMedia();
    }

    public Call<ListMedia> createListMedia(ListMedia listMedia) {
        return listMediaApi.createListMedia(listMedia);
    }

    public Call<ListMedia> getListMediaById(Long id) {
        return listMediaApi.getListMediaById(id);
    }

    public Call<ListMedia> updateListMedia(Long id, ListMedia listMedia) {
        return listMediaApi.updateListMedia(id, listMedia);
    }

    public Call<Void> deleteListMedia(Long id) {
        return listMediaApi.deleteListMedia(id);
    }
}
