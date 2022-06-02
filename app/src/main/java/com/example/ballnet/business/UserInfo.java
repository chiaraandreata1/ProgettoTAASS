package com.example.ballnet.business;


import java.io.Serializable;
import java.util.Locale;

/*
{"id":1,"email":"c.francesco2013@gmail.com","displayName":"Francesco Neri","picture":"https://lh3.googleusercontent.com/a/AATXAJwJ9uzrO6VK5IJyW2akOho0WtAfm6_VQucnldmP=s96-c","type":"ADMIN"}
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 42L;

    public enum Type {
        ADMIN,
        TEACHER,
        PLAYER;

        public String toPrettyString() {
            return toString().charAt(0) + toString().substring(1).toLowerCase(Locale.ROOT);
        }
    }

    private Long id;
    private String email;
    private String displayName;
    private String picture;
    private Type type;

    public UserInfo() {
    }

    public UserInfo(String email, String displayName) {
        this.id = -1L;
        this.email = email;
        this.displayName = displayName;
    }

    public UserInfo(Long id, String email, String displayName, String picture, Type type) {
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s", displayName, email);
    }
}
