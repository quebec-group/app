package com.quebec.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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


    /** Resizes an image for uploading
     *
     * @param image
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }


    /**
     * Uploads the profile picture by first resizing and compressing the image.
     * @param filepath
     * @param callback
     */
    public void uploadProfilePicture(String filepath, ContentProgressListener callback) {

        Bitmap bitmap = BitmapFactory.decodeFile(filepath);
        Bitmap resized = resize(bitmap, 200, 200);

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filepath);
            resized.compress(Bitmap.CompressFormat.PNG, 100, out);

            File file = new File(filepath);
            uploader.uploadFile(file, PROFILE_IMAGE_PREFIX, callback);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the image from the S3 handler, and returns this through the callback.
     * @param callback
     */
    public void getUserProfilePicture(User user, ContentProgressListener callback) {
    //    uploader.getFile(user.getProfileID(), callback);
    }
}
