package com.quebec.services;

import android.net.Uri;

/**
 * Created by Andy on 25/02/2017.
 */

public class Video {
    private String videoID;

    public Video(String videoID) {
        this.videoID = videoID;
    }

    public String getVideoID() {
        return videoID;
    }

    public Uri getURI() {
        return Uri.parse(getVideoID());
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }
}
