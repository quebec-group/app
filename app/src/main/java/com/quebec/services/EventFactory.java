package com.quebec.services;

import com.quebec.app.Event;
import com.quebec.app.User;

import org.json.JSONArray;
import org.json.JSONException;

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
        final String description = eventDAO.get_DAO_BODY().getString("description");
        final String eventID = eventDAO.get_DAO_BODY().getString("eventID");
        final String location = eventDAO.get_DAO_BODY().getString("location");
        final String time = eventDAO.get_DAO_BODY().getString("time");
        final int likesCount = eventDAO.get_DAO_BODY().getInt("likesCount");
        final boolean likes = eventDAO.get_DAO_BODY().getBoolean("likes");

        JSONArray attendeesArray = new JSONArray(eventDAO.get_DAO_BODY().getString("members"));
        JSONArray videosArray = new JSONArray(eventDAO.get_DAO_BODY().getString("videos"));

        UserListFactory userListFactory = new UserListFactory();
        final ArrayList<User> attendees = userListFactory.userListFactory(new JSONArray(attendeesArray));

        VideoListFactory videoListFactory = new VideoListFactory();
        final ArrayList<Video> videos = videoListFactory.videoListFactory(new JSONArray(videosArray));

        Event event = new Event(title, eventID, location, time, videos, attendees, likes, likesCount);
        return event;
    }
}
