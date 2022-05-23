package com.example.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String message;

        if (authentication != null && authentication.getPrincipal() != null)
            message = "Insufficient permission level";
//            response.sendError(HttpStatus.FORBIDDEN.value(), "Insufficient permission level");
        else
            message = "Authentication required";
//            response.sendError(HttpStatus.FORBIDDEN.value(), "Authentication required");

        response.setHeader("Auth-Error", message);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
    }
}
