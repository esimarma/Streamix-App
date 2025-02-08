package com.example.app_streamix.interfaces;


import java.util.List;
import com.example.app_streamix.models.ListMedia;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ListMediaApi {
    @GET("listMedia")
    Call<List<ListMedia>> getListMedia();

    @POST("listMedia")
    Call<ListMedia> createListMedia(@Body ListMedia listMedia);

    @GET("listMedia/{id}")
    Call<ListMedia> getListMediaById(@Path("id") Long id);

    @PUT("listMedia/{id}")
    Call<ListMedia> updateListMedia(@Path("id") Long id, @Body ListMedia listMedia);

    @DELETE("listMedia/{id}")
    Call<Void> deleteListMedia(@Path("id") Long id);
}