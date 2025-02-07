package com.example.app_streamix.interfaces;


import com.example.app_streamix.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserApi {
    @GET("user")
    Call<List<User>> getUser();

    @GET("user/{id}")
    Call<User> getUserById(@Path("id") Long id);

    /*@PUT("user/{id}")
    Call<User> updateUser(@Path("id") Long id, @Body User listMedia);

    @DELETE("user/{id}")
    Call<Void> deleteUser(@Path("id") Long id);*/
}
