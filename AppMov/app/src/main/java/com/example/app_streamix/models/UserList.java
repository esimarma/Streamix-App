package com.example.app_streamix.models;

import java.util.List;

public class UserList {

    private Long id;
    private User user;
    private String name;
    private String listType;
    private List<ListMedia> listMedia;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public List<ListMedia> getListMedia() {
        return listMedia;
    }

    public void setListMedia(List<ListMedia> listMedia) {
        this.listMedia = listMedia;
    }
}

