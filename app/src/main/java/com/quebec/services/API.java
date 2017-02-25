package com.quebec.services;

import com.quebec.app.Event;
import com.quebec.app.User;

import java.util.List;

/**
 * Created by Andy on 14/02/2017.
 */


public interface API {

    // Profile interactions
    public abstract void createUser(final String userName, final String userEmail, final APICallback<String> response);
    public abstract void setProfileVideo(final String friendID, final APICallback<String> response);

    // User interactions
    public abstract void following(final APICallback<List<User>> response);
    public abstract void followers(final APICallback<List<User>> response);
    public abstract void follow(final String friendID, final APICallback<String> response);
    public abstract void unfollow(final String friendID, final APICallback<String> response);



    // Event interactions
    public abstract void createEvent(final String eventName, final String eventDescription, final String eventVideoURL, final APICallback<String> response);
    public abstract void addUserToEvent(final String eventID, final String friendID, final APICallback<String> response);
    public abstract void removeFromEvent(final String eventID, final APICallback<String> response);
    public abstract void likeEvent(final String eventID,final APICallback<String> response);
    public abstract void unlikeEvent(final String eventID, final APICallback<String> response);
    public abstract void getEvents(final APICallback<List<Event>> response);
    public abstract void addVideoToEvent(final String S3ID, final String eventID, final APICallback<String> response);


}
