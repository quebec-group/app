package com.quebec.services;

/**
 * Created by Andy on 14/02/2017.
 */


public interface API {
    public abstract void createEvent(String eventName, String eventDescription, String eventVideoURL, final APICallback response);
    public abstract void createUser(String userName, String userEmail, final APICallback response);
    public abstract void getFriends(final APICallback response);
    public abstract APIResponse setPictureID(String S3ID);
    public abstract APIResponse setVideoID(String S3ID);
    public abstract APIResponse addFriend(String userID);
    public abstract APIResponse removeFriend(String userID);
    public abstract APIResponse addFriendRequest(String userID);
    public abstract APIResponse getPendingFriendRequests();
    public abstract APIResponse getSentFriendRequests();
    public abstract APIResponse addUserToEvent(String eventName, String userID);
    public abstract APIResponse removeUserFromEvent(String eventName, String userID);

}
