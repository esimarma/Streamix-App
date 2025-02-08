package com.example.app_streamix.repositories;


import com.example.app_streamix.interfaces.RatingApi;
import com.example.app_streamix.models.Rating;
import com.example.app_streamix.utils.ApiConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RatingRepository {
    private static String baseUrl = ApiConstants.BASE_URL;
    private static RatingApi ratingApi;

    // Inicialização do Retrofit
    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ratingApi = retrofit.create(RatingApi.class);
    }

    // Métodos para acessar a API
    public Call<List<Rating>> getRating() {
        return ratingApi.getRating();
    }

    public Call<Rating> createRating(Rating rating) {
        return ratingApi.createRating(rating);
    }

    public Call<Rating> getRatingById(Long id) {
        return ratingApi.getRatingById(id);
    }

    public Call<Rating> updaterating(Long id, Rating rating) {
        return ratingApi.updateRating(id, rating);
    }

    public Call<Void> deleteRating(Long id) {
        return ratingApi.deleteRating(id);
    }
}
