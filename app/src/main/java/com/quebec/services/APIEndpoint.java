package com.quebec.services;

/**
 * Created by Andy on 14/02/2017.
 */

public class APIEndpoint {

    private static final String createUser = "createUser";
    private static final String following = "following";
    private static final String followers = "followers";
    private static final String setProfileVideo = "setProfileVideo";
    private static final String addVideoToEvent = "addVideoToEvent";
    private static final String follow = "follow";
    private static final String unfollow = "unfollow";
    private static final String createEvent = "createEvent";
    private static final String addUserToEvent = "addUserToEvent";
    private static final String removeFromEvent = "removeFromEvent";
    private static final String getEvents = "getEvents";
    private static final String likeEvent = "likeEvent";
    private static final String unlikeEvent = "unlikeEvent";
    private static final String getInfo = "getInfo";

    private static final String POST_METHOD = "POST";

    private String path = "/api/";
    private String method;


    public APIEndpoint (String action) {
        switch(action) {
            case "createUser":
                this.path += this.createUser;
                break;

            case "following":
                this.path += this.following;
                break;

            case "setProfileVideo":
                this.path += this.setProfileVideo;
                break;

            case "addVideoToEvent":
                this.path += this.addVideoToEvent;
                break;

            case "unfollow":
                this.path += this.unfollow;
                break;

            case "follow":
                this.path += this.follow;
                break;

            case "createEvent":
                this.path += this.createEvent;
                break;

            case "addUserToEvent":
                this.path += this.addUserToEvent;
                break;

            case "removeFromEvent":
                this.path += this.removeFromEvent;
                break;

            case "getEvents":
                this.path += this.getEvents;
                break;

            case "likeEvent":
                this.path += this.likeEvent;
                break;

            case "unlikeEvent":
                this.path += this.unlikeEvent;
                break;
            case "getInfo":
                this.path += this.getInfo;
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
