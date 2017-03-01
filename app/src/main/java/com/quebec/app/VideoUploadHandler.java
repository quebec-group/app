package com.quebec.app;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.amazonaws.mobile.content.ContentProgressListener;
import com.quebec.services.AWSWrapper;

import java.io.File;

import static com.quebec.app.EventVideoUploadDetails.getDataColumn;
import static com.quebec.app.EventVideoUploadDetails.isDownloadsDocument;
import static com.quebec.app.EventVideoUploadDetails.isExternalStorageDocument;
import static com.quebec.app.EventVideoUploadDetails.isMediaDocument;

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


    public static String getFullS3Path(File file) {
        return "protected/" + AWSWrapper.getCognitoID() + "/" + VIDEO_FOLDER_PREFIX + file.getName();
    }

    /**
     * Converts from a URI to a complete file path. Handles edge cases for Android KitKat etc.
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

}
