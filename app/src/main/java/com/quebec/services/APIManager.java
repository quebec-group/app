package com.quebec.services;

/**
 * Created by Andy on 14/02/2017.
 */

import android.util.Log;

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
     * @param response
     */
    public void createEvent(String eventTitle, String eventLocation, String videoPath, final APICallback<String> response) {
        final APIEndpoint  endpoint = new APIEndpoint("createEvent");
        final APIRequest request = new APIRequest(endpoint);
        final String eventTime = String.valueOf(System.currentTimeMillis());


        // create the request body
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("title", eventTitle);
            requestBody.put("location", eventLocation);
            requestBody.put("time", eventTime);
            requestBody.put("videoPath", videoPath);
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
                    requestBody.put("arn", SNSManager.getArn());
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
    public void setTrainingVideo(final String S3ID, final APICallback<String> response) {
        final APIEndpoint endpoint = new APIEndpoint("setTrainingVideo");
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
                    response.onSuccess("Successfully changed training video to: " + S3ID);
                }
            }
        });

        service.execute();
    }

    /**
     *
     * @param S3ID
     * @param response
     */
    @Override
    public void setProfilePicture(final String S3ID, final APICallback<String> response) {
        final APIEndpoint endpoint = new APIEndpoint("setProfilePicture");
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
                    response.onSuccess("Successfully changed profile picture to: " + S3ID);
                }
            }
        });

        service.execute();
    }

    @Override
    public void hasCompletedSignUp(final APICallback<Boolean> response) {
        final APIEndpoint endpoint = new APIEndpoint("hasCompletedSignUp");
        final APIRequest request = new APIRequest(endpoint);

        JSONObject requestBody = new JSONObject();
        request.setBody(requestBody.toString());

        Service service = new Service(request, new Service.ServiceCallBack() {
            @Override
            public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {
                Log.d(LOG_TAG, apiResponse.getStatus());
                BaseDAO baseDAO = apiResponse.getResponseBody();
                if (apiResponse.getStatus().equals("200")) {
                    JSONObject jsonObject = baseDAO.get_DAO_BODY();
                    response.onSuccess(jsonObject.getBoolean("hasCompletedSignUp"));
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
    public void following(final String userID, final APICallback<List<User>> response) {
        final APIEndpoint endpoint = new APIEndpoint("following");
        final APIRequest request = requestWithUserID(userID, endpoint);

        userListService(request, response);
    }

    /**
     *
     * @param userID
     * @param response
     */
    @Override
    public void followingCount(final String userID, final APICallback<Integer> response) {
        final APIEndpoint endpoint = new APIEndpoint("followingCount");
        final APIRequest request = requestWithUserID(userID, endpoint);

        countService(request, response);
    }

    /**
     *
     * @param userID
     * @param response
     */
    @Override
    public void followersCount(final String userID, final APICallback<Integer> response) {
        final APIEndpoint endpoint = new APIEndpoint("followersCount");
        final APIRequest request = requestWithUserID(userID, endpoint);

        countService(request, response);
    }

    private APIRequest requestWithUserID(final String userID, final APIEndpoint endpoint) {
        final APIRequest request = new APIRequest(endpoint);

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("userID", userID);
            request.setBody(requestBody.toString());
            Log.d(LOG_TAG, requestBody.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return request;
    }

    @Override
    public void followers(final APICallback<List<User>> response) {
        followers(AWSWrapper.getCognitoID(), response);
    }

    @Override
    public void following(final APICallback<List<User>> response) {
        following(AWSWrapper.getCognitoID(), response);
    }

    /**
     *
     * @param userID
     * @param response
     */
    @Override
    public void followers(final String userID, final APICallback<List<User>> response)  {
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

        userListService(request, response);
    }

    /**
     *
     * @param user
     * @param response
     */
    @Override
    public void follow(final User user, final APICallback<String> response) {
        final APIEndpoint endpoint = new APIEndpoint("follow");
        final APIRequest request = requestWithUserID(user.getUserID(), endpoint);

        Service service = new Service(request, new Service.ServiceCallBack() {
            @Override
            public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {
                Log.d(LOG_TAG, apiResponse.getStatus());
                if (apiResponse.getStatus().equals("200")) {
                    response.onSuccess("Successfully followed: " + user.getUserID());
                }
            }
        });

        service.execute();

    }

    /**
     *
     * @param user
     * @param response
     */
    @Override
    public void unfollow(final User user, final APICallback<String> response) {
        final APIEndpoint endpoint = new APIEndpoint("unfollow");
        final APIRequest request = requestWithUserID(user.getUserID(), endpoint);

        Service service = new Service(request, new Service.ServiceCallBack() {
            @Override
            public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {
                Log.d(LOG_TAG, apiResponse.getStatus());
                if (apiResponse.getStatus().equals("200")) {
                    response.onSuccess("Successfully unfollowed: " + user.getUserID());
                }
            }
        });

        service.execute();
    }


    /**
     *
     * @param event
     * @param userID
     * @param response
     */
    @Override
    public void addUserToEvent(final Event event, final String userID, final APICallback<String> response) {
        final APIEndpoint endpoint = new APIEndpoint("addUserToEvent");
        final APIRequest request = new APIRequest(endpoint);
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("userID", userID);
            requestBody.put("eventID", event.getEventID());
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
     * @param event
     * @param response
     */
    @Override
    public void removeFromEvent(final Event event, final APICallback<String> response) {
        final APIEndpoint endpoint = new APIEndpoint("removeFromEvent");
        final APIRequest request = new APIRequest(endpoint);
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("eventID", event.getEventID());
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
     * @param event
     * @param response
     */
    @Override
    public void likeEvent(final Event event, final APICallback<String> response) {
        final APIEndpoint endpoint = new APIEndpoint("likeEvent");
        final APIRequest request = new APIRequest(endpoint);

        // create the request body
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("eventID", event.getEventID());
            request.setBody(requestBody.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        Service service = new Service(request, new Service.ServiceCallBack() {
            @Override
            public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {
                Log.d(LOG_TAG, apiResponse.getStatus());
                if (apiResponse.getStatus().equals("200")) {
                    response.onSuccess("You liked: " + event.getEventName());
                }
            }
        });

        service.execute();
    }

    /**
     *
     * @param event
     * @param response
     */
    @Override
    public void unlikeEvent(final Event event, final APICallback<String> response) {
        final APIEndpoint endpoint = new APIEndpoint("unlikeEvent");
        final APIRequest request = new APIRequest(endpoint);

        // create the request body
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("eventID", event.getEventID());
            request.setBody(requestBody.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        Service service = new Service(request, new Service.ServiceCallBack() {
            @Override
            public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {
                Log.d(LOG_TAG, apiResponse.getStatus());
                if (apiResponse.getStatus().equals("200")) {
                    response.onSuccess("You unliked: " + event.getEventName());
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
    public void addVideoToEvent(final String S3ID, final int eventID, final APICallback<String> response) {
        final APIEndpoint endpoint = new APIEndpoint("addVideoToEvent");
        final APIRequest request = new APIRequest(endpoint);

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("eventID", eventID);
            requestBody.put("S3ID", S3ID);
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
                final String responseBody = apiResponse.getResponseBody().get_DAO_BODY().toString();
                if (apiResponse.getStatus().equals("200")) {
                    response.onSuccess("Video added to event");
                } else {
                    response.onFailure(responseBody);
                }

            }
        });


        service.execute();
    }

    @Override
    public void getInfo(final APICallback<User> response) {
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
                    JSONObject json = baseDAO.get_DAO_BODY();
                    User currentUser = new User(json.optString("name", ""),
                            json.optString("email",""),
                            json.optString("userID", ""),
                            json.optString("profileID", ""));
                    response.onSuccess(currentUser);
                } else {
                    response.onFailure("Error getting info");
                }

            }
        });


        service.execute();
    }

    @Override
    public void followsMe(final User user, final APICallback<Boolean> response) {
        final APIEndpoint endpoint = new APIEndpoint("followsMe");
        final APIRequest request = requestWithUserID(user.getUserID(), endpoint);

        boolService(request, response);
    }

    @Override
    public void iFollow(final User user, final APICallback<Boolean> response) {
        final APIEndpoint endpoint = new APIEndpoint("doIFollow");
        final APIRequest request = requestWithUserID(user.getUserID(), endpoint);

        boolService(request, response);
    }

    public void find(final String searchString, final APICallback<List<User>> response) {
        final APIEndpoint endpoint = new APIEndpoint("find");
        final APIRequest request = new APIRequest(endpoint);

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("searchString", searchString);
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
     * @param response
     */
    @Override
    public void getEvents(final APICallback<List<Event>> response) {
        final APIEndpoint endpoint = new APIEndpoint("getEvents");
        final APIRequest request = new APIRequest(endpoint);

        eventsListService(request, response);
    }

    /**
     *
     * @param response
     */
    @Override
    public void getAttendedEvents(final APICallback<List<Event>> response) {
        getAttendedEvents(AWSWrapper.getCognitoID(), response);
    }

    /**
     *
     * @param response
     */
    @Override
    public void getAttendedEvents(final String userID, final APICallback<List<Event>> response) {
        final APIEndpoint endpoint = new APIEndpoint("getAttendedEvents");
        final APIRequest request = requestWithUserID(userID, endpoint);

        eventsListService(request, response);
    }


    private void eventsListService(APIRequest request, final APICallback<List<Event>> response) {
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

    private void userListService(APIRequest request, final APICallback<List<User>> response) {
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

    private void countService(final APIRequest request, final APICallback<Integer> response) {
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
                    JSONObject jsonObject = baseDAO.get_DAO_BODY();
                    response.onSuccess(jsonObject.getInt("count"));
                } else {
                    response.onFailure(responseBody);
                }

            }
        });


        service.execute();
    }

    private void boolService(final APIRequest request, final APICallback<Boolean> response) {
        // perform the HTTP request and wait for callback
        Service service = new Service(request, new Service.ServiceCallBack() {
            @Override
            /**
             * onResponseReceived takes the DAO from inside the response, sets the status
             */
            public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException {
                BaseDAO baseDAO = apiResponse.getResponseBody();

                if (apiResponse.getStatus().equals("200")) {
                    JSONObject jsonObject = baseDAO.get_DAO_BODY();
                    response.onSuccess(jsonObject.getBoolean("result"));
                } else {
                    response.onFailure(baseDAO.get_DAO_BODY().toString());
                }

            }
        });


        service.execute();
    }
}
