package com.quebec.services;

import android.util.Log;

import com.quebec.app.Event;
import com.quebec.app.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andy on 25/02/2017.
 */

public class EventListFactory {

    private BaseDAO baseDAO;
    private static String LOG_TAG = EventListFactory.class.getSimpleName();

    public void setEventListDAO (BaseDAO baseDAO) {
        this.baseDAO = baseDAO;
    }


    public ArrayList<Event> eventListFactory() throws JSONException {
        // Get the JSON body of the response
        JSONObject jsonObject = baseDAO.get_DAO_BODY();

        // Our list to return, of events attended by the User
        ArrayList<Event> eventsAttended = new ArrayList<>();

        // Get a JSONArray from the events field
        final String events = jsonObject.getString("events");
        JSONArray eventsArray = new JSONArray(events);

        // Create an event object for every event in the response
        for(int i=0; i<eventsArray.length(); i++) {
            JSONObject currentEvent = new JSONObject(eventsArray.getString(i));
            Log.d(LOG_TAG, currentEvent.toString());

            final String eventID = jsonObject.getString("eventID");
            final String eventLocation = jsonObject.getString("location");
            final int likesCount = jsonObject.getInt("likesCount");
            final boolean likes = jsonObject.getBoolean("likes");
            final String title = jsonObject.getString("title");
            final String members = jsonObject.getString("members");
            final String time = jsonObject.getString("time");

            JSONArray jsonArray = new JSONArray(members);
            UserListFactory userListFactory = new UserListFactory();
            ArrayList<User> currentAttendees = userListFactory.userListFactory(jsonArray);

            JSONArray videoArray = new JSONArray(jsonObject.getString("videos"));
            VideoListFactory videoListFactory = new VideoListFactory();
            ArrayList<Video> videos = videoListFactory.videoListFactory(videoArray);

            Event event = new Event(title, eventID, eventLocation, time, videos, currentAttendees, likes, likesCount);

            eventsAttended.add(event);
        }

        return eventsAttended;

    }
}
