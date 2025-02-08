package com.example.app_streamix.interfaces;

import com.example.app_streamix.models.Media;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MediaApi {

    @GET("/movie/popular")
    Call<List<Media>> getPopularMovies();

    @GET("/movie/top_rated")
    Call<List<Media>> getTopRatedMovies();

    @GET("/tv/popular")
    Call<List<Media>> getPopularSeries();

    @GET("/tv/top_rated")
    Call<List<Media>> getTopRatedSeries();
}
