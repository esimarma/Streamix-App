package com.example.app_streamix.interfaces;

import com.example.app_streamix.models.LoginRequest;
import com.example.app_streamix.models.LoginResponse;
import com.example.app_streamix.models.RegisterRequest;
import com.example.app_streamix.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface AuthApi {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("logout")
    Call<Void> logout(@Header("Authorization") String token);

    @GET("user")
    Call<User> getUser(@Header("Authorization") String token);

    @POST("register")
    Call<Void> register(@Body RegisterRequest request);

}
