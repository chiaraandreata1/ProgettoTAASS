package com.example.ballnet.services;

import com.example.ballnet.business.LoginManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class HttpService {

    private static final HttpService INSTANCE = new HttpService();

    public static HttpService getInstance() {
        return INSTANCE;
    }



    LoginManager loginManager = LoginManager.getInstance();

    public HttpService() {
    }

    public String plainGet(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setUseCaches(false);
        if (loginManager.getToken() != null)
            connection.addRequestProperty("Authorization",
                    String.format("Bearer %s", loginManager.getToken()));

        Map<String, List<String>> requestProperties;
        requestProperties = connection.getRequestProperties();

        System.out.println(requestProperties);

        connection.connect();

        String responseMessage = connection.getResponseMessage();

        int responseCode = connection.getResponseCode();

        int contentLength = connection.getContentLength();

        String line;

        if (connection.getErrorStream() != null) {
            BufferedReader errReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

            while ((line = errReader.readLine()) != null)
                System.err.println(line);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder stringBuilder = new StringBuilder();

        while ((line = reader.readLine()) != null)
            stringBuilder.append(line).append("\n");

        return stringBuilder.toString();
    }

    public String plainGet(String baseUrl, Map<String, Object> params) throws IOException {
        String url;

        if (params != null && !params.isEmpty()) {

            StringBuilder paramsBuilder = new StringBuilder();

            for (String key : params.keySet()) {
                Object val = params.get(key);

                if (val instanceof Collection) {
                    for (Object o : (Collection<?>) val) {
                        if (paramsBuilder.length() > 0)
                            paramsBuilder.append('&');
                        paramsBuilder.append(key).append('=').append(o);
                    }
                } else {
                    if (paramsBuilder.length() > 0)
                        paramsBuilder.append('&');
                    paramsBuilder.append(key).append('=').append(val);
                }
            }

            url = String.format("%s?%s", baseUrl, paramsBuilder);
        } else {
            url = baseUrl;
        }

        return plainGet(url);
    }

    public <T> T jsonGet(String urlString, Class<T> resClass) throws IOException {

        String plain = plainGet(urlString);


        return new Gson().fromJson(plain, resClass);
    }

    public <T> T jsonGet(String urlString,
                         Map<String, Object> params,
                         Class<T> resClass) throws IOException {
        String plain = plainGet(urlString, params);

        return new Gson().fromJson(plain, resClass);
    }

    public <T> List<T> jsonGetList(String urlString,
                                   Map<String, Object> params,
                                   Class<T> resClass) throws IOException {
        String plain = plainGet(urlString, params);

        Type type = TypeToken.getParameterized(List.class, resClass).getType();

//        Type type = new TypeToken<ArrayList<T>>() {
//        }.getType();

        return new Gson().fromJson(plain, type);
    }

}
