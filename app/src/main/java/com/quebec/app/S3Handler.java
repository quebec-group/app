package com.quebec.app;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobile.AWSConfiguration;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.amazonaws.mobile.content.UserFileManager;
import com.amazonaws.regions.Regions;

import java.io.File;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Andrew on 09/02/2017.
 */

public class S3Handler {

    private static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 3;

    public static final String S3_PREFIX_PROTECTED = "protected/";

    private ImageView signup_image_preview;
    private TextView error_text;

    private Uri mCropImageUri;

    /** The user file manager. Used on uploads folder */
    private UserFileManager userFileManager;

    private String folderPath;
    private final CountDownLatch userFileManagerCreatingLatch = new CountDownLatch(1);

    public S3Handler() {
        initialiseBucket(AWSConfiguration.AMAZON_S3_USER_FILES_BUCKET, S3_PREFIX_PROTECTED, AWSConfiguration.AMAZON_S3_USER_FILES_BUCKET_REGION);
    }


    public String createPathProtected(File f, String pathPrefix) {
        return  folderPath + pathPrefix + f.getName();
    }

    private void initialiseBucket(final String bucket, final String prefix, final Regions region) {

        final String identityId = AWSMobileClient.defaultMobileClient()
                .getIdentityManager()
                .getCachedUserID();

        folderPath = identityId + "/";

        AWSMobileClient.defaultMobileClient().createUserFileManager(bucket, prefix, region, new UserFileManager.BuilderResultHandler() {
            @Override
            public void onComplete(final UserFileManager userFileManager) {
                S3Handler.this.userFileManager = userFileManager;
                userFileManagerCreatingLatch.countDown();
            }
        });
    }


    /**
     * Uploads a file to the S3 storage bucket.
     * @param path
     */
    public void uploadFile(final File file, final String filePath, final ContentProgressListener callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    userFileManagerCreatingLatch.await();
                } catch (final InterruptedException ex) {
                    // This thread should never be interrupted.
                    throw new RuntimeException(ex);
                }

                userFileManager.uploadContent(file, folderPath + filePath + file.getName(), callback);
            }
        }).start();

    }

    /**
     * Get
     */
    public void getFile(final String path, final ContentProgressListener callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    userFileManagerCreatingLatch.await();
                } catch (final InterruptedException ex) {
                    // This thread should never be interrupted.
                    throw new RuntimeException(ex);
                }

                userFileManager.getContent(folderPath + path, callback);
            }
        }).start();

    }


}
