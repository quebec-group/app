package com.quebec.services;

/**
 * Created by Andy on 14/02/2017.
 */

import android.util.Log;

import com.google.gson.Gson;

/**
 * APIManger: the interface between the UI and services layer
 *            requires asynchronous callback on front-end by
 *            implementing onSuccess and onFailure
 *
 *
 *            takes an APIResponse object and populates the body
 *
 */
public class APIManager {

    private APIResponse apiResponse;

    public APIManager (APIResponse apiResponse) {
        this.apiResponse = apiResponse;
    }

    private static String LOG_TAG = APIManager.class.getSimpleName();

    public APIResponse createEvent(String eventName, String eventDescription, String eventVideoURL) {
        APIEndpoint createEventEndpoint = new APIEndpoint("createEvent");
        APIRequest createEventRequest = new APIRequest(createEventEndpoint);
        Service createEventService = new Service(createEventRequest);
        try {
            createEventService.test();
            this.apiResponse.setResponseBody(createEventService.getResponseBody());
            Log.d(LOG_TAG, "Forwading response to service");
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());

        }
        return this.apiResponse;
    }

}
