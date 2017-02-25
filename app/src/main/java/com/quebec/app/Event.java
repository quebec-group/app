package com.quebec.app;

import android.os.Parcel;
import android.os.Parcelable;

import com.quebec.services.Video;

import java.util.ArrayList;


/**
 * Created by Andrew on 03/02/2017.
 */

public class Event implements Parcelable {

    private String title;
    private String eventID;
    private String location;
    private String time;
    private ArrayList<Video> videos;
    private String likes;
    private String likesCount;
    private ArrayList<User> attendees;



    public Event(String title, String eventID, String location, String time, ArrayList<Video> videos, ArrayList<User> attendees, boolean likes, int likesCount) {
        this.title = title;
        this.eventID = eventID;
        this.location = location;
        this.time = time;
        this.videos = videos;
        this.attendees = attendees;
    }


    protected Event(Parcel in) {
        title = in.readString();
        //videoURL = in.readString();
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

    public ArrayList<Video> getEventVideos() {
        return videos;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
    }
}
