package com.example.shared.rabbithole;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serializable;

public class RabbitResponse<T> implements Serializable {

    private static final long serialVersionUID = 42L;

    private boolean success;

    private T body;

    private HttpStatus status;

    private String errorMessage;

    public RabbitResponse(T body) {
        this.success = true;
        this.body = body;
        this.status = null;
        this.errorMessage = null;
    }

    public RabbitResponse(HttpStatus status, String errorMessage) {
        this.success = false;
        this.body = null;

        this.status = status;
        this.errorMessage = errorMessage;
    }
//    public RabbitResponse(Exception exception) {
//
//        this.success = false;
//        this.body = null;
//
//        this.errorType = exception.getClass().getCanonicalName();
//        this.errorMessage = exception.getMessage();
//    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void throwIfError() {
        if (!success)
            throw new ResponseStatusException(status, errorMessage);
    }

    public static <T> RabbitResponse<T> notFound(String message) {
        return new RabbitResponse<>(HttpStatus.NOT_FOUND, message);
    }

    public static <T> RabbitResponse<T> badRequest(String message) {
        return new RabbitResponse<>(HttpStatus.BAD_REQUEST, message);
    }

    public static <T> RabbitResponse<T> forbidden(String message) {
        return new RabbitResponse<>(HttpStatus.FORBIDDEN, message);
    }

    public static void check(RabbitResponse<?> response) {

        if (response == null)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        if (!response.success)
            throw new ResponseStatusException(response.status, response.errorMessage);
    }
}
