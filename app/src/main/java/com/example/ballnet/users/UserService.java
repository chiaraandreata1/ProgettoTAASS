package com.example.ballnet.users;

import com.example.ballnet.business.UserInfo;
import com.example.ballnet.services.HttpService;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class UserService {

    private static final UserService INSTANCE = new UserService();

    public static UserService getInstance() {
        return INSTANCE;
    }

    private final HttpService httpService = HttpService.getInstance();

    public UserService() {
    }

    public List<UserInfo> getUsers(Collection<Long> users) throws IOException {
        if (users == null || users.isEmpty())
            return Collections.emptyList();

        HashMap<String, Object> params = new HashMap<>();

        params.put("ids", users);

        List<UserInfo> list = httpService.jsonGetList(
                "http://ball.net:8080/api/v1/user/get-users",
                params,
                UserInfo.class
        );

        return list;
    }

    public List<UserInfo> getSuggestions(String query, int limit) throws IOException {

        HashMap<String, Object> params = new HashMap<>();
        //http://ball.net:8080/api/v1/user/find-players?query=f&limit=5
        params.put("query", query);
        params.put("limit", limit);

        List<UserInfo> users = httpService.jsonGetList(
                "http://ball.net:8080/api/v1/user/find-players",
                params,
                UserInfo.class);

        return users;
    }

}
