package com.quebec.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andy on 25/02/2017.
 */

public class VideoListFactory {
    private static String LOG_TAG = VideoListFactory.class.getSimpleName();
    public ArrayList<Video> videoListFactory(JSONArray videoArray) throws JSONException {
        ArrayList<Video> videos = new ArrayList<>();

        for(int i=0; i<videoArray.length();i++) {

            JSONObject currentVideo = new JSONObject(videoArray.getString(i));
            Video vid = new Video(currentVideo);
            videos.add(vid);
        }
        return videos;
    }
}
