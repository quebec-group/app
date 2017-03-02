package com.quebec.services;

import com.quebec.app.Event;
import com.quebec.app.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andy on 16/02/2017.
 */

public class EventFactory {
    private EventDAO eventDAO;

    public void setEventDAO (EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    public Event eventFactory() throws JSONException {
        final String title = eventDAO.get_DAO_BODY().getString("title");
        final int eventID = eventDAO.get_DAO_BODY().getInt("eventID");
        final String location = eventDAO.get_DAO_BODY().getString("location");
        final String time = eventDAO.get_DAO_BODY().getString("time");
        final boolean likes = eventDAO.get_DAO_BODY().getBoolean("likes");
        final int likesCount = eventDAO.get_DAO_BODY().getInt("likesCount");
        final JSONArray videosJson = eventDAO.get_DAO_BODY().getJSONArray("videos");

        final ArrayList<Video> videos = new ArrayList<>();

        for (int i = 0; i < videosJson.length(); i++) {
            JSONObject json = videosJson.getJSONObject(i);

            videos.add(new Video(json));
        }

        final ArrayList<User> attendees = new ArrayList<>();

        Event event = new Event(title, eventID, location, time, videos, attendees, likes, likesCount);

        return event;
    }
}
