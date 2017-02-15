package com.quebec.services;

import org.json.JSONObject;

/**
 * Created by Andy on 14/02/2017.
 */

public class SuccessResponse {
    private JSONObject responseBody;

    public SuccessResponse (JSONObject json) {
        responseBody = json;
    }

    public JSONObject getResponseBody() {
        return responseBody;
    }
}
