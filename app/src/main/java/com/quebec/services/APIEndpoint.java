package com.quebec.services;

/**
 * Created by Andy on 14/02/2017.
 */

public class APIEndpoint {

    private static final String createUser = "createUser";
    private static final String getFriends = "createFriends";
    private static final String setPictureID = "setPictureID";
    private static final String setVideoID = "setVideoID";
    private static final String addFriend = "addFriend";
    private static final String removeFriend = "removeFriend";
    private static final String addFriendRequest = "addFriendRequest";
    private static final String getPendingFriendRequests = "getPendingFriendRequests";
    private static final String getSentFriendReqeusts = "getPendingFriendRequests";
    private static final String createEvent = "createEvent";
    private static final String addUserToEvent = "addUserToEvent";
    private static final String removeUserFromEvent = "removeUserFromEvent";

    private static final String POST_METHOD = "POST";

    private String path = "/api/";
    private String method;


    public APIEndpoint (String action) {
        switch(action) {
            case "createUser":
                this.path += this.createUser;
                break;

            case "getFriends":
                this.path += this.getFriends;
                break;

            case "setPictureID":
                this.path += this.setPictureID;
                break;

            case "setVideoID":
                this.path += this.setVideoID;
                break;

            case "removeFriend":
                this.path += this.removeFriend;
                break;

            case "addFriendRequest":
                this.path += this.addFriendRequest;
                break;

            case "getPendingFriendRequests":
                this.path += this.getPendingFriendRequests;
                break;

            case "createEvent":
                this.path += this.createEvent;
                break;

            case "addUserToEvent":
                this.path += this.addUserToEvent;
                break;

            case "removeUserFromEvent":
                this.path += this.removeUserFromEvent;
                break;

            case "getSentFriendReqeusts":
                this.path += this.getSentFriendReqeusts;
                break;

            case "addFriend":
                this.path += this.addFriend;
                break;
        }
        this.method = this.POST_METHOD;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

}
