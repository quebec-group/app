package com.quebec;

import android.util.Log;

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
        uploader = new S3Handler();

        CognitoSyncManager client = AWSMobileClient.defaultMobileClient().getSyncManager();
        dataset = client.openOrCreateDataset("user_profile");
    }

    public void uploadProfilePicture(String filepath, ContentProgressListener callback) {
        File file = new File(filepath);
        dataset.remove(PROFILE_KEY);
        dataset.put(PROFILE_KEY, PROFILE_IMAGE_PREFIX + file.getName());
        uploader.uploadFile(file, PROFILE_IMAGE_PREFIX, callback);
    }

    public void getImage(ContentProgressListener callback) {
        String path = dataset.get(PROFILE_KEY);
        Log.e("key", path);
        uploader.getFile(path, callback);
    }
}
