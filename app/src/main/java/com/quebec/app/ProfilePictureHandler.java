package com.quebec.app;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;

import java.io.File;

/**
 * Created by Andrew on 09/02/2017.
 */

public class ProfilePictureHandler {
    private static final String PROFILE_IMAGE_PREFIX = "photos/";
    private static final String PROFILE_KEY = "profile_picture";

    private S3Handler uploader;
    private Dataset dataset;

    public ProfilePictureHandler() {
        uploader = S3Handler.getInstance();

        CognitoSyncManager client = AWSMobileClient.defaultMobileClient().getSyncManager();
        dataset = client.openOrCreateDataset("user_profile");
    }

    /**
     * Shows if the profile picture has been saved as a key-value pair
     * @return
     */
    public boolean profilePictureExists() {
        return (dataset.getSizeInBytes(PROFILE_KEY) > -1);
    }

    /**
     * Uploads the profile picture and stores the filepath as a key-value pair.
     * @param filepath
     * @param callback
     */
    public void uploadProfilePicture(String filepath, ContentProgressListener callback) {
        File file = new File(filepath);
        uploader.uploadFile(file, PROFILE_IMAGE_PREFIX, callback);
    }

    /**
     * Gets the image from the S3 handler, and returns this through the callback.
     * @param callback
     */
    public void getUserProfilePicture(User user, ContentProgressListener callback) {
        // uploader.getFile(user.getProfileID(), callback);
    }
}
