package com.quebec.services;

/**
 * Created by Andy on 14/02/2017.
 */

public class EventDTO {
    private String eventID;
    private String eventDescription;
    private String eventVideoURL;

    public EventDTO(String eventID, String eventDescription, String eventVideoURL) {
        this.eventID = eventID;
        this.eventDescription = eventDescription;
        this.eventVideoURL = eventVideoURL;
    }
}
