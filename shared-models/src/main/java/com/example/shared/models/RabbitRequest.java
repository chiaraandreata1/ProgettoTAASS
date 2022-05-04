package com.example.shared.models;

import java.io.Serializable;

public class RabbitRequest<T> implements Serializable {

    private static final long serialVersionUID = 42L;

    private String requestMessage;

    private T requestBody;

    public RabbitRequest(String requestMessage, T requestBody) {
        this.requestMessage = requestMessage;
        this.requestBody = requestBody;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public T getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(T requestBody) {
        this.requestBody = requestBody;
    }
}
