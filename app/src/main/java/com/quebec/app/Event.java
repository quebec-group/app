package com.quebec.app;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


/**
 * Created by Andrew on 03/02/2017.
 */

public class Event implements Parcelable {

    private String title;
    private String description;
    private String eventID;
    private String location;
    private String time;
    private String videoURL;
    private ArrayList<User> attendees;



    public Event(String title, String description, String eventID, String location, String time, String videoURL, ArrayList<User> attendees) {
        this.title = title;
        this.description = description;
        this.videoURL = "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4";
        this.eventID = eventID;
        this.location = location;
        this.time = time;
        this.videoURL = videoURL;
        this.attendees = attendees;
    }


    protected Event(Parcel in) {
        title = in.readString();
        description = in.readString();
        videoURL = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getEventName() {
        return title;
    }

    public String getEventVideoURL() {
        return videoURL;
    }

    public String getDescription() { return description; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(videoURL);
    }
}
