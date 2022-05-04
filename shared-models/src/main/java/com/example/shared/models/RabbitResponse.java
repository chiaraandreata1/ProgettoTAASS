package com.example.shared.models;

import java.io.Serializable;

public class RabbitResponse<T> implements Serializable {

    private static final long serialVersionUID = 42L;

    private boolean success;

    private T body;

    private String errorType;

    private String errorMessage;

    public RabbitResponse(T body) {
        this.success = true;
        this.body = body;
        this.errorType = null;
        this.errorMessage = null;
    }

    public RabbitResponse(Exception exception) {

        this.success = false;
        this.body = null;

        this.errorType = exception.getClass().getCanonicalName();
        this.errorMessage = exception.getMessage();
    }

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

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
