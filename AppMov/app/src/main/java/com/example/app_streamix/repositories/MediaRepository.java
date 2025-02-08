package com.example.app_streamix.repositories;

import com.example.app_streamix.interfaces.MediaApi;
import com.example.app_streamix.models.Media;
import com.example.app_streamix.utils.ApiConstants;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MediaRepository {
    private static final String baseUrl = ApiConstants.BASE_URL;
    private static MediaApi api;

    // Inicialização do Retrofit com Interceptor
    static {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request newRequest = originalRequest.newBuilder()
                                .addHeader("Authorization", ApiConstants.AUTH_BEARER_TOKEN)
                                .build();
                        return chain.proceed(newRequest);
                    }
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
    public Call<List<Media>> getPopularMovies() {
        return api.getPopularMovies();
    }

    public Call<List<Media>> getTopRatedMovies() {
        return api.getTopRatedMovies();
    }

    public Call<List<Media>> getPopularSeries() {
        return api.getPopularSeries();
    }

    public Call<List<Media>> getTopRatedSeries() {
        return api.getTopRatedSeries();
    }
}