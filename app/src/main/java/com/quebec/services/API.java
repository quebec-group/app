package com.quebec.services;

/**
 * Created by Andy on 14/02/2017.
 */


public interface API {

    // Profile interactions
    public abstract void createUser(final String userName, final String userEmail, final APICallback response);
    public abstract void setProfileVideo(final String friendID, final APICallback response);

    // User interactions
    public abstract void following(final APICallback response);
    public abstract void followers(final APICallback response);
    public abstract void follow(final String friendID, final APICallback response);
    public abstract void unfollow(final String friendID, final APICallback response);



    // Event interactions
    public abstract void createEvent(final String eventName, final String eventDescription, final String eventVideoURL, final APICallback response);
    public abstract void addUserToEvent(final String eventID, final String friendID, final APICallback response);
    public abstract void removeFromEvent(final String eventID, final APICallback response);
    public abstract void likeEvent(final String eventID,final APICallback response);
    public abstract void unlikeEvent(final String eventID, final APICallback response);
    public abstract void getEvents(final APICallback response);
    public abstract void addVideoToEvent(final String S3ID, final String eventID, final APICallback response);


}
