package com.quebec.app;

import android.app.Activity;

import com.amazonaws.mobile.content.ContentProgressListener;


import java.io.File;
import java.io.IOException;

/**
 * Created by Andrew on 09/02/2017.
 */

public class VideoUploadHandler {
    private static final String VIDEO_FOLDER_PREFIX = "videos/";

    private S3Handler uploader;

    public VideoUploadHandler() {
        uploader = new S3Handler();
    }

    /**
     * Uploads the profile picture and stores the filepath as a key-value pair.
     * @param callback
     */
    public void uploadVideo(File video, ContentProgressListener callback) {
        uploader.uploadFile(video, VIDEO_FOLDER_PREFIX, callback);
    }


}
