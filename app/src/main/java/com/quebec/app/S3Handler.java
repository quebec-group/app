package com.quebec.app;

import com.amazonaws.mobile.AWSConfiguration;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.amazonaws.mobile.content.UserFileManager;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import java.io.File;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Andrew on 09/02/2017.
 */

public class S3Handler {

    private static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 3;

    /* Public to allow public URL access to the videos. */
    public static final String S3_PREFIX_PROTECTED = "protected/";

    /** The user file manager. Used on uploads folder */
    private UserFileManager userFileManager;

    private String folderPath;
    private final CountDownLatch userFileManagerCreatingLatch = new CountDownLatch(1);

    private static S3Handler instance;

    private S3Handler() {
        initialiseBucket(AWSConfiguration.AMAZON_S3_USER_FILES_BUCKET, S3_PREFIX_PROTECTED, AWSConfiguration.AMAZON_S3_USER_FILES_BUCKET_REGION);
    }

    public static S3Handler getInstance() {
        if (instance == null) {
            instance = new S3Handler();
        }
        return instance;
    }


    public String getVideoURL(String key) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(AWSConfiguration.AMAZON_S3_USER_FILES_BUCKET, key);
        URL objectURL = userFileManager.s3Client.generatePresignedUrl(request);

        return objectURL.toString();
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
     */
    public void uploadFile(final File file, final String filePath, final ContentProgressListener callback) {
        Monitor.getInstance().s3Call("Uploading file ");

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
     * Retrieve a file from the S3 storage bucket.
     */
    public void getFile(final String path, final ContentProgressListener callback) {

        Monitor.getInstance().s3Call("Getting file ");
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    userFileManagerCreatingLatch.await();
                } catch (final InterruptedException ex) {
                    // This thread should never be interrupted.
                    throw new RuntimeException(ex);
                }



                userFileManager.getContent(path.replaceFirst(S3_PREFIX_PROTECTED, ""), callback);
            }
        }).start();
    }
}
