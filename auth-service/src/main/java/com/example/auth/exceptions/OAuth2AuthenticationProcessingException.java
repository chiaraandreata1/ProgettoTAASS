package com.example.auth.exceptions;

import com.example.auth.models.UserEntity;
import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException {

    public OAuth2AuthenticationProcessingException(String msg, Throwable t) {
        super(msg, t);
    }

    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }

    public OAuth2AuthenticationProcessingException(UserEntity user) {
        this(String.format("Looks like you're signed up with %s account. Please use your %s account to login.",
                user.getProvider(),
                user.getProvider()));
    }
}
