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

    private String path;

    public APIEndpoint (String action) {
        switch(action) {
            case "createUser":
                this.path = this.createUser;

            case "getFriends":
                this.path = this.getFriends;

            case "setPictureID":
                this.path = this.setPictureID;

            case "setVideoID":
                this.path = this.setVideoID;

            case "removeFriend":
                this.path = this.removeFriend;

            case "addFriendRequest":
                this.path = this.addFriendRequest;

            case "getPendingFriendRequests":
                this.path = this.getPendingFriendRequests;

            case "createEvent":
                this.path = this.createEvent;

            case "addUserToEvent":
                this.path = this.addUserToEvent;

            case "removeUserFromEvent":
                this.path = this.removeUserFromEvent;

            case "getSentFriendReqeusts":
                this.path = this.getSentFriendReqeusts;

            case "addFriend":
                this.path = this.addFriend;
        }
    }



}
