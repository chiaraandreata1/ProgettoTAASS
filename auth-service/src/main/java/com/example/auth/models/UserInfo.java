package com.example.auth.models;

import java.util.List;

public class UserInfo {

    private String id, displayName, email, pictureLink;

    private List<UserEntity.Role> roles;

    public UserInfo(String id, String displayName, String email, String pictureLink, List<UserEntity.Role> roles) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.pictureLink = pictureLink;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserEntity.Role> getRoles() {
        return roles;
    }

    public void setRoles(List<UserEntity.Role> roles) {
        this.roles = roles;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }
}
