package com.quebec.app;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Andrew on 03/02/2017.
 */

public class Event implements Parcelable {

    private String name;
    private String description;
    private String videoURL;


    public Event(String name, String description) {
        this.name = name;
        this.description = description;
        this.videoURL = "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4";
    }

    protected Event(Parcel in) {
        name = in.readString();
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

    public String getName() {
        return name;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getDescription() { return description; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(videoURL);
    }
}
