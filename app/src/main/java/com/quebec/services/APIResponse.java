package com.quebec.services;

/**
 * Created by Andy on 14/02/2017.
 */

public abstract class APIResponse<T> {

    public abstract void onSuccess(T responseBody);

    public abstract void onFailure(String message);

    private T responseBody;

    public T getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(T responseBody) {
        this.responseBody = responseBody;
    }
}
