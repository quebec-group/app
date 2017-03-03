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
    public abstract void setTrainingVideo(final String userID, final APICallback<String> response);
    public abstract void setProfilePicture(String S3ID, APICallback<String> response);
    public abstract void hasCompletedSignUp(APICallback<Boolean> response);

    // User interactions
    public abstract void following(final String userID, final APICallback<List<User>> response);
    public abstract void following(final APICallback<List<User>> response);
    public abstract void followsMe(final User user, final APICallback<Boolean> response);
    public abstract void followers(final String userID, final APICallback<List<User>> response);


    void followingCount(String userID, APICallback<Integer> response);

    void followersCount(String userID, APICallback<Integer> response);

    public abstract void followers(final APICallback<List<User>> response);
    public abstract void follow(final User user, final APICallback<String> response);
    public abstract void unfollow(final User user, final APICallback<String> response);

    void addVideoToEvent(String S3ID, int eventID, APICallback<String> response);

    public abstract void getInfo(final APICallback<User> response);

    void iFollow(User user, APICallback<Boolean> response);

    public abstract void find(final String searchString, final APICallback<List<User>> response);



    // Event interactions
    public abstract void createEvent(String eventTitle, String eventLocation, String videoPath, final APICallback<String> response);
    public abstract void addUserToEvent(final Event event, final String userID, final APICallback<String> response);
    public abstract void removeFromEvent(final Event event, final APICallback<String> response);
    public abstract void likeEvent(final Event eventID,final APICallback<String> response);
    public abstract void unlikeEvent(final Event eventID, final APICallback<String> response);
    public abstract void getEvents(final APICallback<List<Event>> response);
    public abstract void getAttendedEvents(APICallback<List<Event>> response);
    public abstract void getAttendedEvents(String userID, APICallback<List<Event>> response);
}
