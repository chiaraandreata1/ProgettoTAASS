package com.example.ballnet.business;

import com.google.gson.Gson;

public final class LoginManager {

    private static final LoginManager INSTANCE = new LoginManager();

    public static LoginManager getInstance() {
        return INSTANCE;
    }


    private String token;

    private UserInfo userInfo;

    private LoginManager() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfo getUser() {
        return this.userInfo;
    }

    public void setUser(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
