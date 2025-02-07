package com.example.app_streamix.interfaces;


import com.example.app_streamix.models.Rating;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RatingApi {
    @GET("rating")
    Call<List<Rating>> getRating();

    @POST("rating")
    Call<Rating> createRating(@Body Rating listMedia);

    @GET("rating/{id}")
    Call<Rating> getRatingById(@Path("id") Long id);

    @PUT("rating/{id}")
    Call<Rating> updateRating(@Path("id") Long id, @Body Rating listMedia);

    @DELETE("rating/{id}")
    Call<Void> deleteRating(@Path("id") Long id);
}

