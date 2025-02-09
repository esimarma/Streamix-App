package com.example.app_streamix.models;

import androidx.annotation.ArrayRes;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.List;

public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    @SerializedName("series_wasted_time_min")
    private Integer seriesWastedTimeMin;
    @SerializedName("movie_wasted_time_min")
    private Integer movieWastedTimeMin;
    private LocalDateTime emailVerifiedAt;
    private List<UserList> lists;
    private List<Rating> rating;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSeriesWastedTimeMin() {
        return seriesWastedTimeMin;
    }

    public void setSeriesWastedTimeMin(Integer seriesWastedTimeMin) {
        this.seriesWastedTimeMin = seriesWastedTimeMin;
    }

    public Integer getMovieWastedTimeMin() {
        return movieWastedTimeMin;
    }

    public void setMovieWastedTimeMin(Integer movieWastedTimeMin) {
        this.movieWastedTimeMin = movieWastedTimeMin;
    }

    public LocalDateTime getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(LocalDateTime emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public List<UserList> getLists() {
        return lists;
    }

    public void setLists(List<UserList> lists) {
        this.lists = lists;
    }

    public List<Rating> getRating() {
        return rating;
    }

    public void setRating(List<Rating> rating) {
        this.rating = rating;
    }
}
