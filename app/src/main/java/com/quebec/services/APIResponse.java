package com.quebec.services;

/**
 * Created by Andy on 14/02/2017.
 */

public class APIResponse<T>  {
    private String status;
    private T responseBody;

    public APIResponse (String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public T getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(T responseBody) {
        this.responseBody = responseBody;
    }
}
