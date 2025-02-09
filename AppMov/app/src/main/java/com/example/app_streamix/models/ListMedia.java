package com.example.app_streamix.models;

import com.google.gson.annotations.SerializedName;

public class ListMedia {

    private Long id;
    @SerializedName("id_list_user")
    private Long userListId;
    private UserList userList;
    @SerializedName("id_media")
    private Integer idMedia;
    @SerializedName("media_type")
    private String mediaType;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserListId() {
        return userListId;
    }
    public void setUserListId(Long userListId) {
        this.userListId = userListId;
    }

    public UserList getUserList() {
        return userList;
    }

    public void setUserList(UserList userList) {
        this.userList = userList;
    }

    public Integer getIdMedia() {
        return idMedia;
    }

    public void setIdMedia(Integer idMedia) {
        this.idMedia = idMedia;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
