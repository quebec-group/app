package com.quebec.services;

/**
 * Created by Andy on 14/02/2017.
 */

import android.util.Log;

import com.quebec.app.Event;
import com.quebec.app.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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

    public void createEvent(String eventName, String eventDescription, String eventVideoURL, final APICallback response) {
        endpoint = new APIEndpoint("createEvent");
        request = new APIRequest(endpoint);


        // create the request body
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("title", eventName);
            requestBody.put("location", eventDescription);
            requestBody.put("time", eventName);
            request.setBody(requestBody.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        // perform the HTTP request and wait for callback
        service = new Service(request, new Service.ServiceCallBack() {
            @Override
            /**
             * onResponseReceived takes the DAO from inside the response, sets the status
             */
            public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {
                Log.d(LOG_TAG, "here");
                if (apiResponse.getStatus().equals("200")) {
                    // pass created event back to the user
                    response.onSuccess("Succesfully created the event!");
                } else {
                    response.onFailure(apiResponse.getResponseBody().get_DAO_BODY().toString());
                }

            }
        });


        service.execute();

    }



    @Override
    public void createUser(String userName, String userEmail, final APICallback response) {
        endpoint = new APIEndpoint("createUser");
        request = new APIRequest(endpoint);


        // create the request body
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("name", userName);
            requestBody.put("email", userEmail);
            request.setBody(requestBody.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        // perform the HTTP request and wait for callback
        service = new Service(request, new Service.ServiceCallBack() {
            @Override
            /**
             * onResponseReceived takes the DAO from inside the response, sets the status
             */
            public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {


                UserDAO userDAO = new UserDAO(apiResponse.getResponseBody().get_DAO_BODY());
                final APIResponse userResponse = new APIResponse(apiResponse.getStatus());
                userResponse.setResponseBody(userDAO);

                if (apiResponse.getStatus().equals("200")) {
                    UserFactory userFactory = new UserFactory();
                    userFactory.setUserDAO(userDAO);
                    User user = userFactory.userFactory();
                    // pass created event back to the user
                    response.onSuccess(user);
                } else {
                    response.onFailure(apiResponse.getResponseBody().get_DAO_BODY().toString());
                }

            }
        });


        service.execute();
    }

    @Override
    public void getFriends(final APICallback response)  {
        endpoint = new APIEndpoint("getFriends");
        request = new APIRequest(endpoint);

        // perform the HTTP request and wait for callback
        service = new Service(request, new Service.ServiceCallBack() {
            @Override
            /**
             * onResponseReceived takes the DAO from inside the response, sets the status
             */
            public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {


                BaseDAO baseDAO = apiResponse.getResponseBody();
                final APIResponse userResponse = new APIResponse(apiResponse.getStatus());
                final String responseBody = apiResponse.getResponseBody().get_DAO_BODY().toString();
                userResponse.setResponseBody(baseDAO);

                if (apiResponse.getStatus().equals("200")) {

                    FriendListFactory friendListFactory = new FriendListFactory();
                    friendListFactory.setFriendListDAO(baseDAO);
                    try {
                        ArrayList<User> friends = friendListFactory.friendListFactory();
                        response.onSuccess(friends);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    response.onFailure(responseBody);
                }

            }
        });


        service.execute();

    }

    @Override
    public void setPictureID(String S3ID, final APICallback response) {

    }

    @Override
    public void setVideoID(String S3ID, final APICallback response) {

    }

    @Override
    public void addFriend(final String userID, final APICallback response) {
        endpoint =  new APIEndpoint("addFriend");
        request = new APIRequest(endpoint);

        // create the request body
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("friendID", userID);
            request.setBody(requestBody.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        service = new Service(request, new Service.ServiceCallBack() {
            @Override
            public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {
                Log.d(LOG_TAG, apiResponse.getStatus());
                if (apiResponse.getStatus().equals("200")) {
                    response.onSuccess(new String("Successfully added: " + userID + " as a friend."));
                }
            }
        });

        service.execute();

    }

    @Override
    public void removeFriend(String userID,final APICallback response) {

    }

    @Override
    public void addFriendRequest(String userID,final APICallback response) {
    }

    @Override
    public void getPendingFriendRequests(final APICallback response) {

    }

    @Override
    public void getSentFriendRequests(final APICallback response) {

    }

    @Override
    public void addUserToEvent(String eventID, String userID, final APICallback response) {
        endpoint =  new APIEndpoint("addUserToEvent");
        request = new APIRequest(endpoint);
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("friendID", userID);
            requestBody.put("eventID", eventID);
            request.setBody(requestBody.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        service = new Service(request, new Service.ServiceCallBack() {
            @Override
            public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {
                Log.d(LOG_TAG, apiResponse.getStatus());
                final String responseBody = apiResponse.getResponseBody().get_DAO_BODY().toString();
                if (apiResponse.getStatus().equals("200")) {
                    response.onSuccess(responseBody);
                } else {
                    response.onFailure(responseBody);
                }
            }
        });

        service.execute();
    }

    @Override
    public void removeUserFromEvent(String eventName, String userID,final APICallback response) {

    }


}
