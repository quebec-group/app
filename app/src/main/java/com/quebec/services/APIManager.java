package com.quebec.services;

/**
 * Created by Andy on 14/02/2017.
 */

import android.os.Handler;
import android.util.Log;

import com.amazonaws.mobile.util.ThreadUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * APIManger: the interface between the UI and services layer
 *            requires asynchronous callback on front-end by
 *            implementing onSuccess and onFailure
 *
 *
 *            takes an APIResponse object and populates the body
 *
 */
public class APIManager implements API {


    private APIEndpoint endpoint;
    private APIRequest request;
    private Service service;



    private static String LOG_TAG = APIManager.class.getSimpleName();

    public void createEvent(final String eventName, String eventDescription, String eventVideoURL, final APICallback<SuccessResponse> response) {
        endpoint = new APIEndpoint("createEvent");
        request = new APIRequest(endpoint);


        // create the request body
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("title", eventName);
            request.setBody(requestBody.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        // perform the HTTP request and wait for callback
        service = new Service(request, new Service.ServiceCallBack() {
            @Override
            public void onResponseReceived(JSONObject responseBody) {
                JSONObject json = new JSONObject();
                json = responseBody;
                // ocnstruct API repsonse bla bla
                final APIResponse<SuccessResponse> successResponseAPIResponse = new APIResponse<>();
                SuccessResponse successResponse = new SuccessResponse(json);
                successResponseAPIResponse.setResponseBody(successResponse);
                response.onSuccess(successResponseAPIResponse);
            }
        });


        service.execute();

    }

    @Override
    public APIResponse createUser(String userName, String userEmail) {
//        endpoint = new APIEndpoint("createUser");
//        request = new APIRequest(endpoint);
//        service = new Service(request);
//
//        try {
//            JSONObject requestBody = new JSONObject();
//            requestBody.put("name", userName);
//            requestBody.put("email", userEmail);
//            request.setBody(requestBody.toString());
//        } catch (JSONException e) {
//            Log.e(LOG_TAG, e.getMessage());
//        }
//
//        try {
//            service.test();
//            Log.d(LOG_TAG,service.getResponseBody().toString());
//            this.response.setResponseBody(service.getResponseBody());
//            this.response.onSuccess(service.getResponseBody().toString());
//
//        } catch (Exception e) {
//            Log.e(LOG_TAG, e.getMessage());
//
//        }

        return null;
    }

    @Override
    public APIResponse getFriends() {
//        endpoint = new APIEndpoint("createUser");
//        request = new APIRequest(endpoint);
//        service = new Service(request);
//
//        try {
//            service.test();
//            this.response.setResponseBody(service.getResponseBody());
//            this.response.onSuccess(service.getResponseBody().toString());
//
//        } catch (Exception e) {
//            Log.e(LOG_TAG, e.getMessage());
//
//        }
//
//        return this.response;
        return null;
    }

    @Override
    public APIResponse setPictureID(String S3ID) {
        return null;
    }

    @Override
    public APIResponse setVideoID(String S3ID) {
        return null;
    }

    @Override
    public APIResponse addFriend(String userID) {
        return null;
    }

    @Override
    public APIResponse removeFriend(String userID) {
        return null;
    }

    @Override
    public APIResponse addFriendRequest(String userID) {
        return null;
    }

    @Override
    public APIResponse getPendingFriendRequests() {
        return null;
    }

    @Override
    public APIResponse getSentFriendRequests() {
        return null;
    }

    @Override
    public APIResponse addUserToEvent(String eventName, String userID) {
        return null;
    }

    @Override
    public APIResponse removeUserFromEvent(String eventName, String userID) {
        return null;
    }


}
