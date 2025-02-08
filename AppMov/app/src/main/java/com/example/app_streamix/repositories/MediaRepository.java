package com.example.app_streamix.repositories;

import com.example.app_streamix.interfaces.MediaApi;
import com.example.app_streamix.models.Media;
import com.example.app_streamix.models.MediaResponse;
import com.example.app_streamix.utils.ApiConstants;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MediaRepository {
    private static final String baseUrl = ApiConstants.BASE_URL_MEDIA;
    private static MediaApi api;

    // Inicialização do Retrofit com Interceptor
    static {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .addHeader("Authorization","Bearer " + ApiConstants.AUTH_BEARER_TOKEN) // Adiciona o Bearer Token
                            .addHeader("accept", "application/json") // Define Accept como JSON
                            .build();

                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client) // Aplica o Interceptor ao Retrofit
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(MediaApi.class);
    }

    // Métodos para acessar a API
    public Call<Media> getByIdMovie(int id) {
        return api.getByIdMovie(id);
    }

    public Call<MediaResponse> getPopularMovies() {
        return api.getPopularMovies();
    }

    public Call<MediaResponse> getTopRatedMovies() {
        return api.getTopRatedMovies();
    }

    public Call<Media> getByIdSerie(int id) {
        return api.getByIdSerie(id);
    }

    public Call<MediaResponse> getPopularSeries() {
        return api.getPopularSeries();
    }

    public Call<MediaResponse> getTopRatedSeries() {
        return api.getTopRatedSeries();
    }
}