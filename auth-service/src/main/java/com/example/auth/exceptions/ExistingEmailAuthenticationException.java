package com.example.auth.exceptions;

import org.springframework.security.core.AuthenticationException;

public class ExistingEmailAuthenticationException extends AuthenticationException {

    public ExistingEmailAuthenticationException(String emailID) {
        super(String.format("User with email id %s already exists", emailID));
    }
}
