package com.quebec;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrew on 03/02/2017.
 */

public class Event implements Parcelable {

    private String name;
    private String videoURL;


    public Event(String name) {
        this.name = name;
        this.videoURL = "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4";
    }

    protected Event(Parcel in) {
        name = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(videoURL);
    }
}
