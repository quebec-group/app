package com.quebec.services;

/**
 * Created by Andy on 14/02/2017.
 */

import android.util.Log;

import com.amazonaws.auth.AWSCognitoIdentityProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.regions.Regions;
import com.quebec.app.Event;
import com.quebec.app.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    
    private static String LOG_TAG = APIManager.class.getSimpleName();

    private static APIManager instance;

    private APIManager() {}

    public static APIManager getInstance() {
        if (instance == null) {
            instance = new APIManager();
        }

        return instance;
    }

    /**
     *
     * @param eventTitle
     * @param eventLocation
     * @param eventTime
     * @param response
     */
    public void createEvent(String eventTitle, String eventLocation, String eventTime, final APICallback<String> response) {
        final APIEndpoint  endpoint = new APIEndpoint("createEvent");
        final APIRequest request = new APIRequest(endpoint);


        // create the request body
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("title", eventTitle);
            requestBody.put("location", eventLocation);
            requestBody.put("time", eventTime);
            request.setBody(requestBody.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        // perform the HTTP request and wait for callback
        Service service = new Service(request, new Service.ServiceCallBack() {
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


    /**
     *
     * @param userName
     * @param userEmail
     * @param response
     */
    @Override
    public void createUser(final String userName, final String userEmail, final APICallback<String> response) {
        final APIEndpoint endpoint = new APIEndpoint("createUser");
        final APIRequest request = new APIRequest(endpoint);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // create the request body
                try {
                    JSONObject requestBody = new JSONObject();
                    requestBody.put("name", userName);
                    requestBody.put("email", userEmail);
                    final String arn = SNSManager.getArn();
                    requestBody.put("arn", arn);
                    request.setBody(requestBody.toString());
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }

                // perform the HTTP request and wait for callback
                Service service = new Service(request, new Service.ServiceCallBack() {
                    @Override
                    /**
                     * onResponseReceived takes the DAO from inside the response, sets the status
                     */
                    public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {


                        UserDAO userDAO = new UserDAO(apiResponse.getResponseBody().get_DAO_BODY());
                        final APIResponse userResponse = new APIResponse(apiResponse.getStatus());
                        userResponse.setResponseBody(userDAO);

                        if (apiResponse.getStatus().equals("200")) {
                            // pass created event back to the user
                            response.onSuccess("User successfully created");
                        } else {
                            response.onFailure(apiResponse.getResponseBody().get_DAO_BODY().toString());
                        }

                    }
                });


                service.execute();
            }
        }).start();
    }

    /**
     *
     * @param S3ID
     * @param response
     */
    @Override
    public void setProfileVideo(final String S3ID, final APICallback<String> response) {
        final APIEndpoint endpoint = new APIEndpoint("setProfileVideo");
        final APIRequest request = new APIRequest(endpoint);

        // create the request body
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("S3ID", S3ID);
            request.setBody(requestBody.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        Service service = new Service(request, new Service.ServiceCallBack() {
            @Override
            public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {
                Log.d(LOG_TAG, apiResponse.getStatus());
                if (apiResponse.getStatus().equals("200")) {
                    response.onSuccess(new String("Successfully changed profile video to: " + S3ID));
                }
            }
        });

        service.execute();
    }




    /**
     *
     * @param userID
     * @param response
     */
    @Override
    public void following(final String userID, final APICallback<ArrayList<User>> response) {
        final APIEndpoint endpoint = new APIEndpoint("following");
        final APIRequest request = new APIRequest(endpoint);

        // create the request body
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("userID", userID);
            request.setBody(requestBody.toString());
            Log.d(LOG_TAG, requestBody.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        // perform the HTTP request and wait for callback
        Service service = new Service(request, new Service.ServiceCallBack() {
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

                    UserListFactory userListFactory = new UserListFactory();
                    JSONObject jsonObject = baseDAO.get_DAO_BODY();
                    String s = jsonObject.getString("users");
                    JSONArray jsonArray = new JSONArray(s);
                    try {
                        ArrayList<User> following =  userListFactory.userListFactory(jsonArray);
                        Log.d(LOG_TAG, "following size: " + following.size()+"");
                        response.onSuccess(following);
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

    /**
     *
     * @param userID
     * @param response
     */
    @Override
    public void followers(final String userID, final APICallback<ArrayList<User>> response)  {
        final APIEndpoint endpoint = new APIEndpoint("followers");
        final APIRequest request = new APIRequest(endpoint);

        // create the request body
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("userID", userID);
            request.setBody(requestBody.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        // perform the HTTP request and wait for callback
        Service service = new Service(request, new Service.ServiceCallBack() {
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

                    UserListFactory userListFactory = new UserListFactory();
                    JSONObject jsonObject = baseDAO.get_DAO_BODY();
                    String s = jsonObject.getString("users");
                    JSONArray jsonArray = new JSONArray(s);
                    try {
                        ArrayList<User> followers =  userListFactory.userListFactory(jsonArray);
                        Log.d(LOG_TAG, "followers size: " + followers.size()+"");
                        response.onSuccess(followers);
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

    /**
     *
     * @param userID
     * @param response
     */
    @Override
    public void follow(final String userID, final APICallback<String> response) {
        final APIEndpoint endpoint = new APIEndpoint("follow");
        final APIRequest request = new APIRequest(endpoint);

        // create the request body
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("userID", userID);
            request.setBody(requestBody.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        Service service = new Service(request, new Service.ServiceCallBack() {
            @Override
            public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {
                Log.d(LOG_TAG, apiResponse.getStatus());
                if (apiResponse.getStatus().equals("200")) {
                    response.onSuccess(new String("Successfully followed: " + userID));
                }
            }
        });

        service.execute();

    }

    /**
     *
     * @param friendID
     * @param response
     */
    @Override
    public void unfollow(final String friendID, final APICallback<String> response) {
        final APIEndpoint endpoint = new APIEndpoint("unfollow");
        final APIRequest request = new APIRequest(endpoint);

        // create the request body
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("userID", friendID);
            request.setBody(requestBody.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        Service service = new Service(request, new Service.ServiceCallBack() {
            @Override
            public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {
                Log.d(LOG_TAG, apiResponse.getStatus());
                if (apiResponse.getStatus().equals("200")) {
                    response.onSuccess(new String("Successfully unfollowed: " + friendID));
                }
            }
        });

        service.execute();
    }


    /**
     *
     * @param eventID
     * @param userID
     * @param response
     */
    @Override
    public void addUserToEvent(final String eventID, final String userID, final APICallback<String> response) {
        final APIEndpoint endpoint = new APIEndpoint("addUserToEvent");
        final APIRequest request = new APIRequest(endpoint);
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("userID", userID);
            requestBody.put("eventID", eventID);
            request.setBody(requestBody.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Service service = new Service(request, new Service.ServiceCallBack() {
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

    /**
     *
     * @param eventID
     * @param response
     */
    @Override
    public void removeFromEvent(final String eventID, final APICallback<String> response) {
        final APIEndpoint endpoint = new APIEndpoint("removeFromEvent");
        final APIRequest request = new APIRequest(endpoint);
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("eventID", eventID);
            request.setBody(requestBody.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Service service = new Service(request, new Service.ServiceCallBack() {
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

    /**
     *
     * @param eventID
     * @param response
     */
    @Override
    public void likeEvent(final String eventID, final APICallback<String> response) {
        final APIEndpoint endpoint = new APIEndpoint("likeEvent");
        final APIRequest request = new APIRequest(endpoint);

        // create the request body
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("eventID", eventID);
            request.setBody(requestBody.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        Service service = new Service(request, new Service.ServiceCallBack() {
            @Override
            public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {
                Log.d(LOG_TAG, apiResponse.getStatus());
                if (apiResponse.getStatus().equals("200")) {
                    response.onSuccess(new String("You liked: " + eventID));
                }
            }
        });

        service.execute();
    }

    /**
     *
     * @param eventID
     * @param response
     */
    @Override
    public void unlikeEvent(final String eventID, final APICallback<String> response) {
        final APIEndpoint endpoint = new APIEndpoint("unlikeEvent");
        final APIRequest request = new APIRequest(endpoint);

        // create the request body
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("eventID", eventID);
            request.setBody(requestBody.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        Service service = new Service(request, new Service.ServiceCallBack() {
            @Override
            public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {
                Log.d(LOG_TAG, apiResponse.getStatus());
                if (apiResponse.getStatus().equals("200")) {
                    response.onSuccess(new String("You unliked: " + eventID));
                }
            }
        });

        service.execute();
    }

    /**
     *
     * @param response
     */
    @Override
    public void getEvents(final APICallback<ArrayList<Event>> response) {
        final APIEndpoint endpoint = new APIEndpoint("getEvents");
        final APIRequest request = new APIRequest(endpoint);

        // perform the HTTP request and wait for callback
        Service service = new Service(request, new Service.ServiceCallBack() {
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

                    EventListFactory eventListFactory = new EventListFactory();
                    eventListFactory.setEventListDAO(baseDAO);
                    try {
                        ArrayList<Event> events = eventListFactory.eventListFactory();
                        response.onSuccess(events);
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

    /**
     *
     * @param S3ID
     * @param eventID
     * @param response
     */
    @Override
    public void addVideoToEvent(final String S3ID, final String eventID, final APICallback<String> response) {

    }


    @Override
    public void getInfo(final APICallback<User> response){
        final APIEndpoint endpoint = new APIEndpoint("getInfo");
        final APIRequest request = new APIRequest(endpoint);

        // perform the HTTP request and wait for callback
        Service service = new Service(request, new Service.ServiceCallBack() {
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
                    try {
                        User current_user = UserFactory.getInstance().userFactory(baseDAO.get_DAO_BODY());
                        response.onSuccess(current_user);
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


}
