package com.quebec.services;

/**
 * Created by Andy on 14/02/2017.
 */


public interface API {
    public abstract void createEvent(String eventName, String eventDescription, String eventVideoURL, final APICallback response);
    public abstract void createUser(String userName, String userEmail, final APICallback response);
    public abstract void getFriends(final APICallback response);
    public abstract void setPictureID(String S3ID, final APICallback response);
    public abstract void setVideoID(String S3ID, final APICallback response);
    public abstract void addFriend(String userID, final APICallback response);
    public abstract void removeFriend(String userID, final APICallback response);
    public abstract void addFriendRequest(String userID, final APICallback response);
    public abstract void getPendingFriendRequests(final APICallback response);
    public abstract void getSentFriendRequests(final APICallback response);
    public abstract void addUserToEvent(String eventName, String userID, final APICallback response);
    public abstract void removeUserFromEvent(String eventName, String userID, final APICallback response);

}
