package com.example.app_streamix.interfaces;

import com.example.app_streamix.models.Media;
import com.example.app_streamix.models.MediaResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MediaApi {

    @GET("movie/{id}")
    Call<Media> getByIdMovie(@Path("id") int id);

    @GET("movie/popular")
    Call<MediaResponse> getPopularMovies();

    @GET("movie/top_rated")
    Call<MediaResponse> getTopRatedMovies();

    @GET("tv/{id}")
    Call<Media> getByIdSerie(@Path("id") int id);

    @GET("tv/popular")
    Call<MediaResponse> getPopularSeries();

    @GET("tv/top_rated")
    Call<MediaResponse> getTopRatedSeries();

    @GET("search/{media_type}")
    Call<MediaResponse> searchMedia(@Path("media_type") String mediaType, @Query("query") String query);

}
