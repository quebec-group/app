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
    public abstract void setProfileVideo(final String userID, final APICallback<String> response);

    // User interactions
    public abstract void following(final String userID, final APICallback<List<User>> response);
    public abstract void following(final APICallback<List<User>> response);
    public abstract void followers(final String userID, final APICallback<List<User>> response);
    public abstract void followers(final APICallback<List<User>> response);
    public abstract void follow(final String userID, final APICallback<String> response);
    public abstract void unfollow(final String userID, final APICallback<String> response);
    public abstract void getInfo(final APICallback<User> response);
    public abstract void find(final String searchString, final APICallback<List<User>> response);



    // Event interactions
    public abstract void createEvent(String eventTitle, String eventLocation, String eventTime, String videoPath, final APICallback<String> response);
    public abstract void addUserToEvent(final Event event, final String userID, final APICallback<String> response);
    public abstract void removeFromEvent(final Event event, final APICallback<String> response);
    public abstract void likeEvent(final Event eventID,final APICallback<String> response);
    public abstract void unlikeEvent(final Event eventID, final APICallback<String> response);
    public abstract void getEvents(final APICallback<List<Event>> response);
    public abstract void addVideoToEvent(final String S3ID, final Event event, final APICallback<String> response);


}
