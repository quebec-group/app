package com.quebec.services;

/**
 * Created by Andy on 14/02/2017.
 */

public class APIRequest {
    private String body;
    private APIEndpoint apiEndpoint;

    public APIRequest(APIEndpoint apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public APIEndpoint getApiEndpoint() {
        return apiEndpoint;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


}
