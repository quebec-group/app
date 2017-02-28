package com.quebec.app;

import com.amazonaws.mobile.content.ContentProgressListener;
import com.quebec.services.AWSWrapper;

import java.io.File;

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


    public static String getFullS3PathForFile(File file) {
        return "protected/" + AWSWrapper.getCognitoID() + "/" + VIDEO_FOLDER_PREFIX + file.getName();
    }
}
