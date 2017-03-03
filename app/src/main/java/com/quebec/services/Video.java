package com.quebec.services;

import com.amazonaws.mobile.content.ContentProgressListener;
import com.quebec.app.S3Handler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andy on 25/02/2017.
 */

public class Video {
    private int videoID;
    private String videoPath;
    private String thumbnailPath;


    public Video(JSONObject json) throws JSONException {
        videoID = json.getInt("videoID");
        videoPath = json.getString("videoPath");
        thumbnailPath = json.getString("thumbnailPath");
    }

    public int getVideoID() {
        return videoID;
    }

    public void setVideoID(int videoID) {
        this.videoID = videoID;
    }

    public String getVideoPath() { return videoPath; }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void getEventThumbnail(ContentProgressListener callback) {
            S3Handler.getInstance().getFile(getThumbnailPath(), callback);
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}
