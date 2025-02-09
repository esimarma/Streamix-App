package com.example.app_streamix.interfaces;

import java.util.List;

import com.example.app_streamix.models.ApiResponse;
import com.example.app_streamix.models.UserList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserListApi {
    @GET("userList")
    Call<List<UserList>> getUserList();

    @POST("userList")
    Call<UserList> createUserList(@Body UserList listMedia);

    @GET("userList/{id}")
    Call<UserList> getUserListById(@Path("id") Long id);

    @GET("user-lists/type/{listType}/{userId}")
    Call<ApiResponse<UserList>> getByListType(@Path("listType") String listType, @Path("userId") Long userId);

    @PUT("userList/{id}")
    Call<UserList> updateUserList(@Path("id") Long id, @Body UserList listMedia);

    @DELETE("userList/{id}")
    Call<Void> deleteUserList(@Path("id") Long id);
}