package com.example.app_streamix.interfaces;


import java.util.List;
import com.example.app_streamix.models.ListMedia;

import retrofit2.Call;
import retrofit2.http.GET;import retrofit2.http.GET;

public interface ListMediaApi {
    @GET("listMedia")
    Call<List<ListMedia>> getListMedia();
}
