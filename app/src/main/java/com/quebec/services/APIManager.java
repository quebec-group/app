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


                EventDAO eventDAO = (EventDAO) apiResponse.getResponseBody();
                final APIResponse eventResponse = new APIResponse(apiResponse.getStatus());
                eventResponse.setResponseBody(eventDAO);

                if (apiResponse.getStatus() == "success") {
                    EventFactory eventFactory = new EventFactory();
                    eventFactory.setEventDAO(eventDAO);
                    Event event = eventFactory.eventFactory();
                    // pass created event back to the user
                    response.onSuccess(event);
                } else {
                    response.onFailure("Failed to create the event!");
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


                UserDAO userDAO = (UserDAO) apiResponse.getResponseBody();
                final APIResponse userResponse = new APIResponse(apiResponse.getStatus());
                userResponse.setResponseBody(userDAO);

                if (apiResponse.getStatus() == "success") {
                    UserFactory userFactory = new UserFactory();
                    userFactory.setUserDAO(userDAO);
                    User user = userFactory.userFactory();
                    // pass created event back to the user
                    response.onSuccess(user);
                } else {
                    response.onFailure("Failed to create the user!");
                }

            }
        });


        service.execute();
    }

    @Override
    public void getFriends(final APICallback response)  {
        endpoint = new APIEndpoint("getFriends");
        request = new APIRequest(endpoint);


        // create the request body
        request.setBody(new String());

        // perform the HTTP request and wait for callback
        service = new Service(request, new Service.ServiceCallBack() {
            @Override
            /**
             * onResponseReceived takes the DAO from inside the response, sets the status
             */
            public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {


                BaseDAO baseDAO = apiResponse.getResponseBody();
                final APIResponse userResponse = new APIResponse(apiResponse.getStatus());
                userResponse.setResponseBody(baseDAO);

                if (apiResponse.getStatus() == "success") {

                    FriendListFactory friendListFactory = new FriendListFactory();
                    friendListFactory.setFriendListDAO(baseDAO);
                    try {
                        ArrayList<User> friends = friendListFactory.friendListFactory();
                        response.onSuccess(friends);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    response.onFailure("Failed to create the user!");
                }

            }
        });


        service.execute();

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
