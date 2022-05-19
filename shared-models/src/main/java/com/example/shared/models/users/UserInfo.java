package com.example.shared.models.users;

public class UserInfo {

    private Long id;
    private String email;
    private String displayName;
    private String picture;

    private UserType type;

    public UserInfo(Long id, String email, String displayName, String picture, UserType type) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.picture = picture;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }
}
