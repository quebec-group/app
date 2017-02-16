package com.quebec.services;

import com.quebec.app.Event;
import com.quebec.app.User;

import org.json.JSONException;

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
        final String videoID = eventDAO.get_DAO_BODY().getString("videoID");
        Event event = new Event(title, description, eventID, location, time, videoID);
        return event;
    }
}
