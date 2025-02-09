package com.example.app_streamix.repositories;


import com.example.app_streamix.interfaces.UserListApi;
import com.example.app_streamix.models.ApiResponse;
import com.example.app_streamix.models.UserList;
import com.example.app_streamix.utils.ApiConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserListRepository {
    private static String baseUrl = ApiConstants.BASE_URL;
    private static UserListApi userListApi;

    // Inicialização do Retrofit
    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userListApi = retrofit.create(UserListApi.class);
    }

    // Métodos para acessar a API
    public Call<List<UserList>> getUserList() {
        return userListApi.getUserList();
    }

    public Call<UserList> createUserList(UserList userList) {
        return userListApi.createUserList(userList);
    }

    public Call<UserList> getUserListById(Long id) {
        return userListApi.getUserListById(id);
    }

    public Call<ApiResponse<UserList>> getByListType(String listType, Long userId) {
        return userListApi.getByListType(listType, userId);
    }

    public Call<UserList> updateUserList(Long id, UserList userList) {
        return userListApi.updateUserList(id, userList);
    }

    public Call<Void> deleteUserList(Long id) {
        return userListApi.deleteUserList(id);
    }
}

