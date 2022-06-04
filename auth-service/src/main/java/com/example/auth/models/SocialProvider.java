package com.example.auth.models;

import com.example.auth.models.oauth2.FacebookOAuth2UserInfo;
import com.example.auth.models.oauth2.GithubOAuth2UserInfo;
import com.example.auth.models.oauth2.GoogleOAuth2UserInfo;
import com.example.auth.models.oauth2.LinkedinOAuth2UserInfo;

import java.util.Map;

public enum SocialProvider {

    FACEBOOK("facebook", FacebookOAuth2UserInfo::new),
    TWITTER("twitter", GithubOAuth2UserInfo::new),
    LINKEDIN("linkedin", LinkedinOAuth2UserInfo::new),
    GOOGLE("google", GoogleOAuth2UserInfo::new),
    GITHUB("github", GithubOAuth2UserInfo::new);

    private String providerType;
    private OAuth2UserInfo.Inflater inflater;

    SocialProvider(String providerType, OAuth2UserInfo.Inflater inflater) {
        this.providerType = providerType;
        this.inflater = inflater;
    }

    public String getProviderType() {
        return providerType;
    }

    public static SocialProvider search(String string) {
        for (SocialProvider provider : values())
            if (provider.providerType.equalsIgnoreCase(string) || String.format("%s-m", provider.providerType).equalsIgnoreCase(string))
                return provider;
        return null;
    }

    public OAuth2UserInfo inflate(Map<String, Object> stringObjectMap) {
        return inflater.apply(stringObjectMap);
    }
}
