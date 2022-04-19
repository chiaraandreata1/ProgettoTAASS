package com.example.auth.models;

import java.util.Map;
import java.util.function.Function;

public abstract class OAuth2UserInfo {

    public interface Inflater extends Function<Map<String, Object>, OAuth2UserInfo> {}

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();

}
