package com.quebec.services;

import android.util.Log;

import com.quebec.app.Event;
import com.quebec.app.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

            final int eventID = currentEvent.getInt("eventID");
            final String eventLocation = currentEvent.getString("location");
            final int likesCount = currentEvent.getInt("likesCount");
            final boolean likes = currentEvent.getBoolean("likes");
            final String title = currentEvent.getString("title");
            final String members = currentEvent.getString("members");
            final String time = currentEvent.getString("time");
            final JSONObject creator = currentEvent.getJSONObject("creator");

            User creatorUser = new User();
            creatorUser.setEmail(creator.getString("email"));
            creatorUser.setUserID(creator.getString("userID"));
            creatorUser.setName(creator.getString("name"));
            creatorUser.setProfileID(creator.getString("profileID"));

            JSONArray jsonArray = new JSONArray(members);
            UserListFactory userListFactory = new UserListFactory();
            ArrayList<User> currentAttendees = userListFactory.userListFactory(jsonArray);

            JSONArray videoArray = new JSONArray(currentEvent.getString("videos"));
            VideoListFactory videoListFactory = new VideoListFactory();
            ArrayList<Video> videos = videoListFactory.videoListFactory(videoArray);

            Event event = new Event(title, eventID, eventLocation, time, videos, currentAttendees, likes, likesCount);
            event.setCreator(creatorUser);

            eventsAttended.add(event);
        }

        Collections.sort(eventsAttended, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o2.getTime().compareTo(o1.getTime());
            }
        });

        return eventsAttended;

    }
}
