package com.quebec.services;

/**
 * Created by Andy on 14/02/2017.
 */

public class APIResponse<T> {
    private T responseBody;

    public T getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(T responseBody) {
        this.responseBody = responseBody;
    }
}
