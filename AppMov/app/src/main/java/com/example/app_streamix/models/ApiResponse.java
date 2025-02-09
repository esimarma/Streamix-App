package com.example.app_streamix.models;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {
    @SerializedName("data")
    private T data;

    public T getData() {
        return data;
    }
}