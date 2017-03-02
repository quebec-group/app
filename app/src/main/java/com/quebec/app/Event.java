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
    private int eventID;
    private String location;
    private String time;
    private ArrayList<Video> videos;
    private boolean likes;
    private int likesCount;
    private ArrayList<User> attendees;
    private User creator;



    public Event(String title, int eventID, String location, String time, ArrayList<Video> videos, ArrayList<User> attendees, boolean likes, int likesCount) {
        this.title = title;
        this.eventID = eventID;
        this.location = location;
        this.time = time;
        this.videos = videos;
        this.attendees = attendees;
        this.likes = likes;
        this.likesCount = likesCount;
    }

    public String printEvent() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("(title:" + this.title + "),");
        sb.append("(eventID:" + this.eventID + "),");
        sb.append("(location:" + this.location + "),");
        sb.append("(time:" + this.time + "),");
        sb.append("(likes:" + this.likes + "),");
        sb.append("(likesCount:" + this.likesCount + "),");
        sb.append("(attendees:");
        for(User u : attendees) {
            sb.append("(" + u.getName() + ")");
        }
        return sb.toString();

    }

    protected Event(Parcel in) {
        title = in.readString();
        eventID = in.readInt();
        location = in.readString();
        time = in.readString();
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

    public ArrayList<User> getAttendees() {
        return attendees;
    }

    public int getLikesCount() { return likesCount; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(eventID);
        dest.writeString(location);
        dest.writeString(time);
    }

    public boolean getLikes() {
        return likes;
    }

    public int getEventID() {
        return eventID;
    }

    public String getTime() {
        return time;
    }

    public void setLikes(boolean likes) {
        this.likes = likes;
    }
    
    public String getLocation() { return location; }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
