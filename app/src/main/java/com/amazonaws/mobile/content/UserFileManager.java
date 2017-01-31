//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.14
//
package com.amazonaws.mobile.content;

import android.content.Context;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobile.util.ThreadUtils;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * UserFileManager inherits from ContentManager and adds upload capabilities.
 *
 * The User File Manager uploads and downloads files from Amazon S3. It caches
 * downloaded files locally on the device in a size-limited cache. Downloaded
 * files may be pinned to the cache, so that they are not automatically removed
 * when the cache size limit is exceeded. The User File Manager provides access
 * to two folders in the Amazon S3 bucket, one called "public/" for public files,
 * which are accessible to any user of the app, and one called "private/" which
 * contains a sub-folder for each Amazon Cognito identified user. Files in the
 * user's private folder can only be accessed by that user. The User File Manager
 * serves as the application's interface into the file-related functionality
 * of the User Data Storage feature.
 */
public class UserFileManager extends ContentManager {
    final String LOG_TAG = UserFileManager.class.getSimpleName();

    /**
     * Provides the result of creating the user file manager back to the application.
     */
    public interface BuilderResultHandler {
        void onComplete(UserFileManager userFileManager);
    }

    /**
     * Builder for convenience of instantiation.
     */
    public static final class Builder {
        private Context context = null;
        private IdentityManager identityManager = null;
        private String bucket = null;
        private String s3ObjectDirPrefix = null;
        private String basePath = null;
        private ClientConfiguration clientConfiguration;
        private Regions region;
        /**
         * Provides the application contenxt.
         * @param context application context
         * @return builder
         */
        public Builder withContext(final Context context) {
            this.context = context;
            return this;
        }

        /**
         * Provides the identity manager for bootstrapping service calls.
         * @param identityManager identity manager
         * @return builder
         */
        public Builder withIdentityManager(final IdentityManager identityManager) {
            this.identityManager = identityManager;
            return this;
        }

        /**
         * Provides the Amazon S3 bucket.
         * @param s3Bucket Amazon S3 bucket
         * @return builder
         */
        public Builder withS3Bucket(final String s3Bucket) {
            this.bucket = s3Bucket;
            return this;
        }

        /**
         * Provides the file path prefix for files to consider in this user
         * file manager. Only files in the Amazon S3 bucket that start with
         * this prefix will be considered within the scope of this user file
         * manager instance.
         * @param s3ObjectDirPrefix folder prefix in Amazon S3 bucket
         * @return builder
         */
        public Builder withS3ObjectDirPrefix(final String s3ObjectDirPrefix) {
            this.s3ObjectDirPrefix = s3ObjectDirPrefix;
            return this;
        }

        /**
         * Provides the local on-device file path for where the user file manager
         * should store its downloaded files.
         * @param basePath local cache folder path
         * @return builder
         */
        public Builder withLocalBasePath(final String basePath) {
            this.basePath = basePath;
            return this;
        }

        public Builder withClientConfiguration(final ClientConfiguration clientConfiguration) {
            this.clientConfiguration = clientConfiguration;
            return this;
        }

        public Builder withRegion(final Regions region){
            this.region = region;
            return this;
        }

        /**
         * Builds and initializes the user file manager instance in the background
         * and notifies the results handler when the operation is completed. The
         * results handler is invoke on the UI thread.
         * @param resultHandler handler for initialization completion event
         */
        public void build(final BuilderResultHandler resultHandler) {
            if (clientConfiguration == null) {
                clientConfiguration = new ClientConfiguration();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final UserFileManager userFileManager =
                        new UserFileManager(context, identityManager, bucket, s3ObjectDirPrefix, null, basePath,
                                clientConfiguration, region);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultHandler.onComplete(userFileManager);
                        }
                    });
                }
            }).start();
        }
    }

    /**
     * Constructs a user file manager.
     *
     * @param context an Android context.
     * @param identityManager identity manager to use for credentials.
     * @param bucket the s3 bucket.
     * @param s3ObjectDirPrefix the directory within the bucket for which this content manager will
     * manage content. Should not contain a trailing '/'This may be null if the root directory of
     * the bucket should be used. The object delimiter is always the standard directory separator of
     * '/'.
     * @param cloudFrontDomainName The CloudFront domain name where this bucket's content may be
     * retrieved by downloading over http from a CloudFront edge location.
     * @param basePath the base path under which to store the files managed by this content manager.
     * This path will have a subdirectory identifying the remote location, and beneath that
     * subdirectories 'content' and 'incoming' will be created to store the locally cached content
     * and incoming
     */
    UserFileManager(final Context context, final IdentityManager identityManager,
                    final String bucket, final String s3ObjectDirPrefix,
                    final String cloudFrontDomainName, final String basePath,
                    final ClientConfiguration clientConfiguration, final Regions regions) {
        super(context, identityManager, bucket, s3ObjectDirPrefix, cloudFrontDomainName, basePath, regions,
                clientConfiguration);
    }

    /**
     * Uploads content by file name to an S3 bucket. The key is the base name of the file prepended
     * with s3DirPrefix.
     *
     * @param file the file to upload.
     * @param filePath the relative path and name of the file to be uploaded
     * @param listener progress listener
     */
    public void uploadContent(final File file, final String filePath, final ContentProgressListener listener) {
        transferHelper.upload(file, filePath, listener);
    }

    /**
     * Handles a response to an operation which invokes some service call.
     */
    public interface ResponseHandler {
        /**
         * Indicates the operation was a success.
         */
        void onSuccess();

        /**
         * Indicates the operation failed and provides the error.
         * @param exception exception
         */
        void onError(AmazonClientException exception);
    }

    /**
     * Deletes the file from the Amazon S3 bucket and notifies the application
     * of the result of the operation. The response handler is invoked on the UI
     * thread.
     * @param filePath path to the file
     * @param handler response handler used to provide the application with the
     *        result of the asynchronous operation
     */
    public void deleteRemoteContent(final String filePath, final ResponseHandler handler) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    s3Client.deleteObject(bucket, getS3PathPrefix(filePath));
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handler.onSuccess();
                        }
                    });
                } catch (final AmazonClientException ace) {
                    Log.e(LOG_TAG, "Failed to delete " + filePath, ace);
                    if (handler != null) {
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handler.onError(ace);
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * Creates a folder in the Amazon S3 bucket and notifies the appliction of
     * the result of the operation. The response handler is invoked on the UI
     * thread.
     * @param folderPath path of the folder to create
     * @param handler response handler used to provide the application with the
     *        result of the asynchronous operation
     */
    public void createFolder(final String folderPath, final ResponseHandler handler) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final InputStream is = new ByteArrayInputStream(new byte[0]);
                    ObjectMetadata meta = new ObjectMetadata();
                    meta.setContentLength(0);
                    s3Client.putObject(bucket, getS3PathPrefix(folderPath), is, meta);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handler.onSuccess();
                        }
                    });
                } catch (final AmazonClientException ace) {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handler.onError(ace);
                        }
                    });
                }
            }
        });
    }
}
